package parroquia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RepositorioDatos {

    private static RepositorioDatos instancia;
    private final Map<String, Persona> personas;
    private final List<Inscripcion> inscripciones;

    private RepositorioDatos() {
        this.personas = new LinkedHashMap<>();
        this.inscripciones = new ArrayList<>();
    }

    public static synchronized RepositorioDatos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDatos();
        }
        return instancia;
    }

    public static synchronized void reiniciarInstancia() {
        instancia = null;
    }

    public void registrarPersona(Persona persona) {
        if (personas.containsKey(persona.getDni())) {
            throw new RegistroDuplicadoException("Ya existe una persona registrada con ese DNI.");
        }
        personas.put(persona.getDni(), persona);
    }

    public void actualizarPersona(String dniAnterior, Persona personaActualizada) {
        if (!personas.containsKey(dniAnterior)) {
            throw new RegistroNoEncontradoException("No existe una persona registrada con ese DNI.");
        }

        if (!dniAnterior.equals(personaActualizada.getDni())
                && personas.containsKey(personaActualizada.getDni())) {
            throw new RegistroDuplicadoException("Ya existe una persona registrada con el nuevo DNI.");
        }

        personas.remove(dniAnterior);
        personas.put(personaActualizada.getDni(), personaActualizada);
    }

    public void eliminarPersona(String dni) {
        if (!personas.containsKey(dni)) {
            throw new RegistroNoEncontradoException("No existe una persona registrada con ese DNI.");
        }
        personas.remove(dni);
    }

    public Persona buscarPersona(String dni) {
        Persona persona = personas.get(dni);

        if (persona == null) {
            throw new RegistroNoEncontradoException("No existe ninguna persona con ese DNI.");
        }

        return persona;
    }

    public void registrarInscripcion(Inscripcion inscripcion) {
        Sacramento sacramento = inscripcion.getSacramento();

        if (sacramento instanceof Matrimonio matrimonio) {
            validarMatrimonio(matrimonio);
        } else {
            validarHorarioDisponible(sacramento);
        }

        if (sacramento instanceof Retiro retiro) {
            validarCupoDeRetiro(retiro);
        }

        inscripciones.add(inscripcion);
    }

    private void validarHorarioDisponible(Sacramento nuevoSacramento) {
        for (Inscripcion inscripcion : inscripciones) {
            Sacramento existente = inscripcion.getSacramento();

            if (existente.getFechaProgramada().equals(nuevoSacramento.getFechaProgramada())
                    && existente.getHora().equals(nuevoSacramento.getHora())) {
                throw new RegistroDuplicadoException(
                        "Ya existe un evento programado en esa fecha y hora.");
            }
        }
    }

    private void validarMatrimonio(Matrimonio nuevo) {
        String dni1 = nuevo.getBeneficiario().getDni();
        String dni2 = nuevo.getContrayente().getDni();

        boolean repetido1 = false;
        boolean repetido2 = false;
        boolean horarioOcupado = false;

        for (Inscripcion inscripcion : inscripciones) {
            Sacramento existente = inscripcion.getSacramento();

            if (existente.getFechaProgramada().equals(nuevo.getFechaProgramada())
                    && existente.getHora().equals(nuevo.getHora())) {
                horarioOcupado = true;
            }

            if (existente instanceof Matrimonio matrimonio) {
                String anterior1 = matrimonio.getBeneficiario().getDni();
                String anterior2 = matrimonio.getContrayente().getDni();

                if (dni1.equals(anterior1) || dni1.equals(anterior2)) {
                    repetido1 = true;
                }

                if (dni2.equals(anterior1) || dni2.equals(anterior2)) {
                    repetido2 = true;
                }
            }
        }

        if (horarioOcupado && repetido1 && repetido2) {
            throw new RegistroDuplicadoException(
                    "Los dos contrayentes ya se encuentran registrados y esa fecha y hora ya estan ocupadas.");
        }

        if (horarioOcupado && (repetido1 || repetido2)) {
            throw new RegistroDuplicadoException(
                    "Esa fecha y hora ya estan ocupadas y uno de los contrayentes ya se encuentra registrado.");
        }

        if (horarioOcupado) {
            throw new RegistroDuplicadoException(
                    "Ya existe un evento programado en esa fecha y hora.");
        }

        if (repetido1 && repetido2) {
            throw new RegistroDuplicadoException(
                    "Los dos contrayentes ya se encuentran registrados en un matrimonio.");
        }

        if (repetido1 || repetido2) {
            throw new RegistroDuplicadoException(
                    "Uno de los contrayentes ya se encuentra registrado en un matrimonio.");
        }
    }

    public List<Inscripcion> inscripcionesRetiroEn(LocalDate fecha) {
        return inscripciones.stream()
                .filter(i -> i.getSacramento() instanceof Retiro)
                .filter(i -> i.getSacramento().getFechaProgramada().equals(fecha))
                .toList();
    }

    public Integer capacidadRetiroEn(LocalDate fecha) {
        return inscripcionesRetiroEn(fecha).stream()
                .map(i -> ((Retiro) i.getSacramento()).getCuposTotales())
                .findFirst()
                .orElse(null);
    }

    public int cuposOcupadosRetiroEn(LocalDate fecha) {
        return inscripcionesRetiroEn(fecha).size();
    }

    public int cuposDisponiblesRetiroEn(LocalDate fecha) {
        Integer capacidad = capacidadRetiroEn(fecha);

        if (capacidad == null) {
            return 0;
        }

        return Math.max(0, capacidad - cuposOcupadosRetiroEn(fecha));
    }

    private void validarCupoDeRetiro(Retiro retiro) {
        LocalDate fecha = retiro.getFechaProgramada();
        String dni = retiro.getBeneficiario().getDni();

        boolean yaInscrito = inscripcionesRetiroEn(fecha).stream()
                .anyMatch(i -> i.getSacramento().getBeneficiario().getDni().equals(dni));

        if (yaInscrito) {
            throw new RegistroDuplicadoException(
                    "Esa persona ya esta inscrita en el retiro de la fecha " + fecha + ".");
        }

        Integer capacidad = capacidadRetiroEn(fecha);

        if (capacidad == null) {
            return;
        }

        if (retiro.getCuposTotales() != capacidad) {
            throw new DatosInvalidosException(
                    "El retiro del " + fecha + " ya fue abierto con " + capacidad + " cupos.");
        }

        if (cuposOcupadosRetiroEn(fecha) >= capacidad) {
            throw new DatosInvalidosException(
                    "No hay cupos disponibles para el retiro del " + fecha + ".");
        }
    }

    public List<Inscripcion> getInscripciones() {
        return List.copyOf(inscripciones);
    }

    public int getTotalPersonas() {
        return personas.size();
    }

    public Path guardarRespaldo(String ruta) throws IOException {
        StringBuilder contenido = new StringBuilder();

        contenido.append("Respaldo de inscripciones - Parroquia Sagrada Familia")
                .append(System.lineSeparator());

        contenido.append("Total de personas registradas: ")
                .append(getTotalPersonas())
                .append(System.lineSeparator());

        contenido.append("-----------------------------------------------------")
                .append(System.lineSeparator());

        for (Inscripcion inscripcion : inscripciones) {
            contenido.append(inscripcion.aLineaTexto())
                    .append(System.lineSeparator());
        }

        Path path = Path.of(ruta);
        Files.writeString(path, contenido.toString());

        return path;
    }
}
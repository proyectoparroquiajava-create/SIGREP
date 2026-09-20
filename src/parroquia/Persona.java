package parroquia;

import java.time.LocalDate;
import java.util.regex.Pattern;


public abstract class Persona {

    private static final Pattern PATRON_NOMBRE =
    		 Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,60}$");

    private final String nombreCompleto;
    private final String dni;
    private final String telefono;
    private final LocalDate fechaRegistro;
    

    protected Persona(String nombreCompleto, String dni, String telefono) {

        validarNombre(nombreCompleto);
        validarDni(dni);
        validarTelefono(telefono);
        
        this.nombreCompleto = nombreCompleto.trim();
        this.dni = dni;
        this.telefono = telefono;
        this.fechaRegistro = LocalDate.now();
    }

    private static void validarNombre(String nombre) {

        if (nombre == null || !PATRON_NOMBRE.matcher(nombre.trim()).matches()) {

            throw new DatosInvalidosException(
            		 "El nombre completo debe contener solo letras y espacios (2 a 60 caracteres).");
        }
    }

    private static void validarDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new DatosInvalidosException(
                    "El DNI debe tener 8 digitos numericos.");
        }
    }

    private static void validarTelefono(String telefono) {
        if (telefono == null || !telefono.matches("9\\d{8}")) {
            throw new DatosInvalidosException(
                    "El telefono debe tener 9 digitos númericos y empezar con 9.");
        }
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public abstract String describir();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(nombre='" + nombreCompleto + "')";
    }
}
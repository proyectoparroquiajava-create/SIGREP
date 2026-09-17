package parroquia;

import java.util.List;
import java.util.stream.Collectors;


public final class Reportes {

    private Reportes() {
    }

    public static List<Inscripcion> filtrarPorTipo(List<Inscripcion> inscripciones, String tipo) {
        String tipoNormalizado = tipo.trim().toLowerCase();
        return inscripciones.stream()
                .filter(i -> i.getSacramento().tipo().toLowerCase().contains(tipoNormalizado))
                .collect(Collectors.toList());
    }

   
    public static List<String> obtenerResumenes(List<Inscripcion> inscripciones) {
        return inscripciones.stream()
                .map(i -> i.getSacramento().resumen())
                .collect(Collectors.toList());
    }

   
    public static double calcularTotalRecaudado(List<Inscripcion> inscripciones) {
        return inscripciones.stream()
                .reduce(0.0,
                        (acumulado, i) -> acumulado + (i.getRecibo() != null ? i.getRecibo().monto() : 0.0),
                        Double::sum);
    }

  
    public static ReporteTipo generarReportePorTipo(List<Inscripcion> inscripciones, String tipo) {
        List<Inscripcion> filtradas = filtrarPorTipo(inscripciones, tipo);
        return new ReporteTipo(
                tipo,
                filtradas.size(),
                obtenerResumenes(filtradas),
                calcularTotalRecaudado(filtradas));
    }

    
    public record ReporteTipo(String tipo, int cantidad, List<String> detalle, double totalRecaudado) {
    }
}

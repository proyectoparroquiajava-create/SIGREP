package parroquia;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase de demostracion (no forma parte del nucleo del dominio).
 * Se crea unicamente como evidencia de ejecucion para el informe
 * tecnico: registra personas, crea los tres tipos de sacramentos,
 * emite recibos, genera reportes funcionales y guarda el respaldo,
 * dejando ver en consola el comportamiento real del sistema SIGREP.
 */
public class DemoEjecucionSIGREP {

    public static void main(String[] args) throws Exception {
        RepositorioDatos.reiniciarInstancia();
        RepositorioDatos repo = RepositorioDatos.getInstancia();

        System.out.println("==============================================");
        System.out.println(" SIGREP - Demostracion de ejecucion del nucleo");
        System.out.println("==============================================");

        // 1) Registro de personas (feligreses y sacerdote)
        Sacerdote padreJulio = new Sacerdote("Julio Alberto Ramos", "40551234", "987001122", "Parroco");
        Feligres ana = new Feligres("Ana Maria Torres", "45551111", "987112233", "Jr. Las Flores 120, Callao");
        Feligres luis = new Feligres("Luis Fernando Vega", "45552222", "987223344", "Av. Saenz Pena 450, Callao");
        Feligres carmen = new Feligres("Carmen Rosa Diaz", "45553333", "987334455", "Calle Lima 88, Callao");
        Feligres jorge = new Feligres("Jorge Luis Paredes", "45554444", "987445566", "Jr. Colon 210, Callao");
        Feligres rosa = new Feligres("Rosa Elvira Nunez", "45555555", "987556677", "Av. Guardia Chalaca 300, Callao");

        for (Persona p : List.of(padreJulio, ana, luis, carmen, jorge, rosa)) {
            repo.registrarPersona(p);
            System.out.println("Registrado -> " + p.describir());
        }
        System.out.println("Total de personas registradas: " + repo.getTotalPersonas());
        System.out.println();

        // 2) Bautizo (usa la fabrica SacramentoFactory)
        Sacramento bautizo = SacramentoFactory.crear(
                "bautizo", ana, padreJulio, LocalDate.now().plusDays(20), 80.0, luis, carmen);
        Inscripcion inscBautizo = new Inscripcion(bautizo);
        Recibo reciboBautizo = inscBautizo.emitirRecibo();
        repo.registrarInscripcion(inscBautizo);
        System.out.println("[Bautizo] " + bautizo.resumen());
        System.out.println("  Recibo: " + reciboBautizo.numero() + " - S/ " + reciboBautizo.monto());

        // 3) Matrimonio (requiere Feligres + 2 testigos)
        Sacramento matrimonio = SacramentoFactory.crear(
                "matrimonio", jorge, padreJulio, LocalDate.now().plusDays(45), 350.0,
                rosa, List.of(ana, luis));
        Inscripcion inscMatrimonio = new Inscripcion(matrimonio);
        Recibo reciboMatrimonio = inscMatrimonio.emitirRecibo();
        repo.registrarInscripcion(inscMatrimonio);
        System.out.println("[Matrimonio] " + matrimonio.resumen());
        System.out.println("  Recibo: " + reciboMatrimonio.numero() + " - S/ " + reciboMatrimonio.monto());

        // 4) Retiro espiritual con control de cupos (2 inscritos de 3 cupos)
        LocalDate fechaRetiro = LocalDate.now().plusDays(10);
        Sacramento retiro1 = SacramentoFactory.crear("retiro", carmen, padreJulio, fechaRetiro, 45.0, 2);
        Inscripcion inscRetiro1 = new Inscripcion(retiro1);
        inscRetiro1.emitirRecibo();
        repo.registrarInscripcion(inscRetiro1);

        Sacramento retiro2 = SacramentoFactory.crear("retiro", rosa, padreJulio, fechaRetiro, 45.0, 2);
        Inscripcion inscRetiro2 = new Inscripcion(retiro2);
        inscRetiro2.emitirRecibo();
        repo.registrarInscripcion(inscRetiro2);

        System.out.println("[Retiro] cupos ocupados: " + repo.cuposOcupadosRetiroEn(fechaRetiro)
                + " / " + repo.capacidadRetiroEn(fechaRetiro)
                + " (disponibles: " + repo.cuposDisponiblesRetiroEn(fechaRetiro) + ")");
        System.out.println();

        // 5) Manejo de excepciones de negocio (evidencia de robustez)
        System.out.println("--- Validacion de reglas de negocio (excepciones esperadas) ---");
        probarExcepcion("DNI duplicado", () -> repo.registrarPersona(
                new Feligres("Otra Persona Cualquiera", "45551111", "987000000", "Jr. Prueba 1")));
        probarExcepcion("Nombre con formato invalido", () ->
                new Feligres("Ana123", "45559999", "987000001", "Jr. Prueba 2"));
        probarExcepcion("Tercer inscrito supera el cupo del retiro", () -> {
            Sacramento retiro3 = SacramentoFactory.crear("retiro", jorge, padreJulio, fechaRetiro, 45.0, 2);
            Inscripcion i = new Inscripcion(retiro3);
            i.emitirRecibo();
            repo.registrarInscripcion(i);
        });
        probarExcepcion("Tipo de sacramento no soportado", () ->
                SacramentoFactory.crear("primera comunion", ana, padreJulio, LocalDate.now().plusDays(1), 30.0));
        System.out.println();

        // 6) Reportes con programacion funcional (filter, map, reduce)
        System.out.println("--- Reportes (Stream API: filter / map / reduce) ---");
        Reportes.ReporteTipo repBautizos = Reportes.generarReportePorTipo(repo.getInscripciones(), "bautizo");
        System.out.println("Bautizos -> cantidad: " + repBautizos.cantidad());
        repBautizos.detalle().forEach(l -> System.out.println("   " + l));

        Reportes.ReporteTipo repRetiros = Reportes.generarReportePorTipo(repo.getInscripciones(), "retiro");
        System.out.println("Retiros -> cantidad: " + repRetiros.cantidad()
                + " | recaudado: S/ " + repRetiros.totalRecaudado());

        double totalGeneral = Reportes.calcularTotalRecaudado(repo.getInscripciones());
        System.out.println("Total recaudado (todas las inscripciones): S/ " + totalGeneral);
        System.out.println();

        // 7) Respaldo en disco
        var ruta = repo.guardarRespaldo("respaldo_sigrep_demo.txt");
        System.out.println("Respaldo escrito en: " + ruta.toAbsolutePath());

        System.out.println();
        System.out.println("==============================================");
        System.out.println(" Fin de la demostracion. Ver PruebasAutomatizadasSIGREP");
        System.out.println(" para el detalle de verificaciones tipo unidad.");
        System.out.println("==============================================");
    }

    private interface Accion {
        void ejecutar();
    }

    private static void probarExcepcion(String descripcion, Accion accion) {
        try {
            accion.ejecutar();
            System.out.println("[INESPERADO] " + descripcion + " -> no se lanzo ninguna excepcion.");
        } catch (ExcepcionSistemaParroquial ex) {
            System.out.println("[OK] " + descripcion + " -> " + ex.getClass().getSimpleName()
                    + ": " + ex.getMessage());
        }
    }
}

package parroquia;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pruebas automatizadas (JUnit 4) del nucleo de dominio de SIGREP.
 * Cubren: validaciones de Persona, control de cupos de Retiro,
 * emision de Recibo y reportes funcionales de Reportes.
 */
public class PruebasAutomatizadasSIGREP {

    private Sacerdote sacerdote;
    private Feligres feligres1;
    private Feligres feligres2;
    private Feligres feligres3;

    @Before
    public void inicializar() {
        RepositorioDatos.reiniciarInstancia();
        sacerdote = new Sacerdote("Julio Alberto Ramos", "40551234", "987001122");
        feligres1 = new Feligres("Ana Maria Torres", "45551111", "987112233", "Jr. Las Flores 120");
        feligres2 = new Feligres("Luis Fernando Vega", "45552222", "987223344", "Av. Saenz Pena 450");
        feligres3 = new Feligres("Carmen Rosa Diaz", "45553333", "987334455", "Calle Lima 88");
    }

    @Test
    public void feligresValidoSeConstruyeCorrectamente() {
        assertEquals("Ana Maria Torres", feligres1.getNombreCompleto());
        assertEquals("45551111", feligres1.getDni());
        assertEquals("Feligres: Ana Maria Torres", feligres1.describir());
    }

    @Test(expected = DatosInvalidosException.class)
    public void dniConLetrasLanzaExcepcion() {
        new Feligres("Pedro Paz", "4555A111", "987112233", "Jr. Las Flores 120");
    }

    @Test(expected = DatosInvalidosException.class)
    public void nombreConNumerosLanzaExcepcion() {
        new Feligres("Pedro123 Paz", "45551199", "987112233", "Jr. Las Flores 120");
    }

    @Test(expected = RegistroDuplicadoException.class)
    public void registrarDosVecesElMismoDniLanzaExcepcion() {
        RepositorioDatos repo = RepositorioDatos.getInstancia();
        repo.registrarPersona(feligres1);
        repo.registrarPersona(new Feligres("Otro Nombre", "45551111", "987000000", "Otra direccion"));
    }

    @Test
    public void bautizoCalculaResumenYRecibo() {
        Sacramento bautizo = SacramentoFactory.crear(
                "bautizo", feligres1, sacerdote, LocalDate.now().plusDays(15), 80.0, feligres2, feligres3);
        Inscripcion inscripcion = new Inscripcion(bautizo);
        Recibo recibo = inscripcion.emitirRecibo();

        assertEquals("Bautizo", bautizo.tipo());
        assertEquals(80.0, recibo.monto(), 0.001);
        assertTrue(bautizo.resumen().contains("Ana Maria Torres"));
    }

    @Test(expected = DatosInvalidosException.class)
    public void emitirReciboDosVecesLanzaExcepcion() {
        Sacramento bautizo = SacramentoFactory.crear(
                "bautizo", feligres1, sacerdote, LocalDate.now().plusDays(15), 80.0, feligres2, feligres3);
        Inscripcion inscripcion = new Inscripcion(bautizo);
        inscripcion.emitirRecibo();
        inscripcion.emitirRecibo();
    }

    @Test
    public void retiroRespetaElCupoMaximo() {
        RepositorioDatos repo = RepositorioDatos.getInstancia();
        LocalDate fecha = LocalDate.now().plusDays(10);

        Sacramento r1 = SacramentoFactory.crear("retiro", feligres1, sacerdote, fecha, 45.0, 2);
        Sacramento r2 = SacramentoFactory.crear("retiro", feligres2, sacerdote, fecha, 45.0, 2);
        repo.registrarInscripcion(new Inscripcion(r1));
        repo.registrarInscripcion(new Inscripcion(r2));

        assertEquals(0, repo.cuposDisponiblesRetiroEn(fecha));

        Sacramento r3 = SacramentoFactory.crear("retiro", feligres3, sacerdote, fecha, 45.0, 2);
        Inscripcion i3 = new Inscripcion(r3);
        assertThrows(DatosInvalidosException.class, () -> repo.registrarInscripcion(i3));
    }

    @Test(expected = DatosInvalidosException.class)
    public void matrimonioConMenosDeDosTestigosLanzaExcepcion() {
        SacramentoFactory.crear(
                "matrimonio", feligres1, sacerdote, LocalDate.now().plusDays(30), 350.0,
                feligres2, List.of(feligres3));
    }

    @Test(expected = TipoSacramentoNoSoportadoException.class)
    public void tipoDeSacramentoInexistenteLanzaExcepcion() {
        SacramentoFactory.crear("confirmacion", feligres1, sacerdote, LocalDate.now().plusDays(5), 20.0);
    }

    @Test
    public void reportePorTipoFiltraYSumaCorrectamente() {
        RepositorioDatos repo = RepositorioDatos.getInstancia();

        Sacramento bautizo = SacramentoFactory.crear(
                "bautizo", feligres1, sacerdote, LocalDate.now().plusDays(15), 80.0, feligres2, feligres3);
        Inscripcion ib = new Inscripcion(bautizo);
        ib.emitirRecibo();
        repo.registrarInscripcion(ib);

        Sacramento retiro = SacramentoFactory.crear(
                "retiro", feligres2, sacerdote, LocalDate.now().plusDays(9), 45.0, 5);
        Inscripcion ir = new Inscripcion(retiro);
        ir.emitirRecibo();
        repo.registrarInscripcion(ir);

        Reportes.ReporteTipo reporte = Reportes.generarReportePorTipo(repo.getInscripciones(), "bautizo");
        assertEquals(1, reporte.cantidad());
        assertEquals(80.0, reporte.totalRecaudado(), 0.001);

        double total = Reportes.calcularTotalRecaudado(repo.getInscripciones());
        assertEquals(125.0, total, 0.001);
    }

    @Test(expected = DatosInvalidosException.class)
    public void reciboConMontoNegativoLanzaExcepcion() {
        new Recibo("R-TEST", -10.0, java.time.LocalDateTime.now(), "Prueba");
    }
}

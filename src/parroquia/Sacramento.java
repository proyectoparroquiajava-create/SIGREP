package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Sacramento {

    private final Persona beneficiario;
    private final Sacerdote sacerdote;
    private final LocalDate fechaProgramada;
    private final LocalTime hora;
    private final double costo;

    protected Sacramento(Persona beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora, double costo) {

        if (beneficiario == null) {
            throw new DatosInvalidosException(
                    "El beneficiario debe ser una Persona registrada.");
        }

        if (sacerdote == null) {
            throw new DatosInvalidosException(
                    "Debe seleccionar un sacerdote encargado.");
        }

        if (fechaProgramada == null
                || fechaProgramada.isBefore(LocalDate.now())) {

            throw new DatosInvalidosException(
                    "La fecha programada no puede ser anterior a hoy.");
        }

        if (hora == null) {
            throw new DatosInvalidosException(
                    "Debe ingresar una hora para el sacramento.");
        }

        if (costo < 0) {
            throw new DatosInvalidosException(
                    "El costo no puede ser negativo.");
        }

        this.beneficiario = beneficiario;
        this.sacerdote = sacerdote;
        this.fechaProgramada = fechaProgramada;
        this.hora = hora;
        this.costo = costo;
    }

    public Persona getBeneficiario() {
        return beneficiario;
    }

    public Sacerdote getSacerdote() {
        return sacerdote;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public LocalTime getHora() {
        return hora;
    }

    public double getCosto() {
        return costo;
    }

    public abstract String tipo();

    public String resumen() {
        return tipo() + " de " + beneficiario.getNombreCompleto()
                + " programado el " + fechaProgramada
                + " a las " + hora;
    }
}
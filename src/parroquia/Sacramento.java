package parroquia;

import java.time.LocalDate;

public abstract class Sacramento {

    private final Persona beneficiario;
    private final Sacerdote sacerdote;
    private final LocalDate fechaProgramada;
    private final double costo;

    protected Sacramento(Persona beneficiario, Sacerdote sacerdote, LocalDate fechaProgramada, double costo) {
        if (beneficiario == null) {
            throw new DatosInvalidosException("El beneficiario debe ser una Persona registrada.");
        }
        if (sacerdote == null) {
            throw new DatosInvalidosException(
                    "Debe seleccionar un sacerdote encargado.");
        }
        if (fechaProgramada == null || fechaProgramada.isBefore(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha programada no puede ser anterior a hoy.");
        }
        if (costo < 0) {
            throw new DatosInvalidosException("El costo no puede ser negativo.");
        }
        this.beneficiario = beneficiario;
        this.sacerdote = sacerdote;
        this.fechaProgramada = fechaProgramada;
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

    public double getCosto() {
        return costo;
    }

  
    public abstract String tipo();

    public String resumen() {
        return tipo() + " de " + beneficiario.getNombreCompleto()
                + " programado el " + fechaProgramada;
    }
}

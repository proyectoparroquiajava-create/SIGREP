package parroquia;

import java.time.LocalDate;

public class Confirmación extends Sacramento {

    private final Persona padrino;

    public Confirmación(Feligres beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, double costo, Persona padrino) {

        super(beneficiario, sacerdote, fechaProgramada, costo);
        this.padrino = padrino;
    }

    public Persona getPadrino() {
        return padrino;
    }

    @Override
    public String tipo() {
        return "Confirmación";
    }

    @Override
    public String resumen() {
        return super.resumen() + " (padrino: " + padrino + ")";
    }
}
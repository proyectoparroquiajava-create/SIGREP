package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;

public class Confirmación extends Sacramento {

    private final Persona padrino;

    public Confirmación(Feligres beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora,
            double costo, Persona padrino) {

        super(beneficiario, sacerdote, fechaProgramada, hora, costo);

        this.padrino = padrino;
    }

    public Persona getPadrino() {
        return padrino;
    }

    public String tipo() {
        return "Confirmación";
    }

    @Override
    public String resumen() {
        return super.resumen() + " (padrino: " + padrino + ")";
    }
}
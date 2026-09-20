package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrimeraComunión extends Sacramento {

    private final Persona padrino;

    public PrimeraComunión(Feligres beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora,
            double costo, Persona padrino) {

        super(beneficiario, sacerdote, fechaProgramada, hora, costo);

        this.padrino = padrino;
    }

    public Persona getPadrino() {
        return padrino;
    }

    @Override
    public String tipo() {
        return "Primera Comunión";
    }

    @Override
    public String resumen() {
        return super.resumen() + " (padrino: " + padrino + ")";
    }
}
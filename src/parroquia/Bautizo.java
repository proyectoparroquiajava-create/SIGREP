package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;

public class Bautizo extends Sacramento {

    private final Persona padrino;
    private final Persona madrina;

    public Bautizo(Persona beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora, double costo,
            Persona padrino, Persona madrina) {

        super(beneficiario, sacerdote, fechaProgramada, hora, costo);

        this.padrino = padrino;
        this.madrina = madrina;
    }

    public Persona getPadrino() {
        return padrino;
    }

    public Persona getMadrina() {
        return madrina;
    }

    @Override
    public String tipo() {
        return "Bautizo";
    }
}
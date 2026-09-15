package parroquia;

import java.time.LocalDate;

public class Bautizo extends Sacramento {

    private final Persona padrino;
    private final Persona madrina;

    public Bautizo(Persona beneficiario, Sacerdote sacerdote,
                   LocalDate fechaProgramada, double costo,
                   Persona padrino, Persona madrina) {

        super(beneficiario, sacerdote, fechaProgramada, costo);

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
package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Matrimonio extends Sacramento {

    private final Feligres contrayente;
    private final List<Persona> testigos;

    public Matrimonio(Feligres beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora, double costo,
            Feligres contrayente, List<Persona> testigos) {

        super(beneficiario, sacerdote, fechaProgramada, hora, costo);

        if (testigos == null || testigos.size() < 2) {
            throw new DatosInvalidosException(
                    "Un matrimonio requiere al menos 2 testigos.");
        }

        this.contrayente = contrayente;
        this.testigos = List.copyOf(testigos);
    }

    public Feligres getContrayente() {
        return contrayente;
    }

    public List<Persona> getTestigos() {
        return testigos;
    }

    @Override
    public String tipo() {
        return "Matrimonio";
    }
}
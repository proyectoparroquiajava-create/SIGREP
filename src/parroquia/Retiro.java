package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;

public class Retiro extends Sacramento {

    private final int cuposTotales;

    public Retiro(Feligres beneficiario, Sacerdote sacerdote,
            LocalDate fechaProgramada, LocalTime hora,
            double costo, int cuposTotales) {

        super(beneficiario, sacerdote, fechaProgramada, hora, costo);

        if (cuposTotales <= 0) {
            throw new DatosInvalidosException(
                    "Un retiro debe tener al menos 1 cupo.");
        }

        this.cuposTotales = cuposTotales;
    }

    public int getCuposTotales() {
        return cuposTotales;
    }

    @Override
    public String tipo() {
        return "Retiro espiritual";
    }

    @Override
    public String resumen() {
        return super.resumen()
                + " (capacidad: " + cuposTotales + " cupos)";
    }
}
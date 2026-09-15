package parroquia;

import java.time.LocalDate;

public class Retiro extends Sacramento {

    private final int cuposTotales;

    public Retiro(Feligres beneficiario, Sacerdote sacerdote,
                  LocalDate fechaProgramada, double costo, int cuposTotales) {

        super(beneficiario, sacerdote, fechaProgramada, costo);

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
        return super.resumen() + " (capacidad: " + cuposTotales + " cupos)";
    }
}
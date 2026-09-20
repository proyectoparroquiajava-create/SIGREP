package parroquia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public final class SacramentoFactory {

    private static final Set<String> TIPOS_SOPORTADOS =
            Set.of("bautizo", "matrimonio", "retiro",
                    "confirmación", "primera comunión");

    private SacramentoFactory() {

    }

    public static Sacramento crear(String tipo, Object... args) {

        String tipoNormalizado =
                tipo == null ? "" : tipo.trim().toLowerCase();

        switch (tipoNormalizado) {

            case "bautizo":
                return new Bautizo(
                        (Persona) args[0],
                        (Sacerdote) args[1],
                        (LocalDate) args[2],
                        (LocalTime) args[3],
                        (Double) args[4],
                        (Persona) args[5],
                        (Persona) args[6]);

            case "matrimonio":

                @SuppressWarnings("unchecked")
                List<Persona> testigos =
                        (List<Persona>) args[6];

                return new Matrimonio(
                        (Feligres) args[0],
                        (Sacerdote) args[1],
                        (LocalDate) args[2],
                        (LocalTime) args[3],
                        (Double) args[4],
                        (Feligres) args[5],
                        testigos);

            case "retiro":
                return new Retiro(
                        (Feligres) args[0],
                        (Sacerdote) args[1],
                        (LocalDate) args[2],
                        (LocalTime) args[3],
                        (Double) args[4],
                        (Integer) args[5]);

            case "primera comunión":
                return new PrimeraComunión(
                        (Feligres) args[0],
                        (Sacerdote) args[1],
                        (LocalDate) args[2],
                        (LocalTime) args[3],
                        (Double) args[4],
                        (Persona) args[5]);

            case "confirmación":
                return new Confirmación(
                        (Feligres) args[0],
                        (Sacerdote) args[1],
                        (LocalDate) args[2],
                        (LocalTime) args[3],
                        (Double) args[4],
                        (Persona) args[5]);

            default:
                throw new TipoSacramentoNoSoportadoException(
                        "El tipo '" + tipo
                        + "' no esta soportado. Use uno de: "
                        + TIPOS_SOPORTADOS);
        }
    }
}

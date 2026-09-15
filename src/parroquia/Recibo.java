package parroquia;

import java.time.LocalDateTime;

public record Recibo(String numero, double monto, LocalDateTime fechaEmision, String concepto) {

    public Recibo {
        if (monto < 0) {
            throw new DatosInvalidosException("El monto del recibo no puede ser negativo.");
        }
    }
}

package parroquia;

import java.time.LocalDateTime;
import java.util.UUID;

public class Inscripcion {

    private final String id;
    private final Sacramento sacramento;
    private Recibo recibo;
    private final LocalDateTime fechaInscripcion;

    public Inscripcion(Sacramento sacramento) {
        this.id = UUID.randomUUID().toString();
        this.sacramento = sacramento;
        this.recibo = null;
        this.fechaInscripcion = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Sacramento getSacramento() {
        return sacramento;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public Recibo emitirRecibo() {
        if (recibo != null) {
            throw new DatosInvalidosException("Esta inscripcion ya tiene un recibo emitido.");
        }
        recibo = new Recibo(
                "R-" + id.substring(0, 8).toUpperCase(),
                sacramento.getCosto(),
                LocalDateTime.now(),
                sacramento.tipo());
        return recibo;
    }

    /** Representacion simple en texto, usada para el respaldo en disco. */
    public String aLineaTexto() {
        return String.format(
                "id=%s | tipo=%s | beneficiario=%s | fechaProgramada=%s | costo=%.2f | recibo=%s",
                id, sacramento.tipo(), sacramento.getBeneficiario().getNombreCompleto(),
                sacramento.getFechaProgramada(), sacramento.getCosto(),
                recibo != null ? recibo.numero() : "pendiente");
    }
}

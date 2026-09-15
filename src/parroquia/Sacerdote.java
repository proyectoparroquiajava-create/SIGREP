package parroquia;


public class Sacerdote extends Persona {

    private final String cargo;

    public Sacerdote(String nombreCompleto, String dni, String telefono, String cargo) {
        super(nombreCompleto, dni, telefono);
        this.cargo = (cargo == null || cargo.isBlank()) ? "Parroco" : cargo;
    }

    public Sacerdote(String nombreCompleto, String dni, String telefono) {
        this(nombreCompleto, dni, telefono, "Parroco");
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public String describir() {
        return cargo + ": " + getNombreCompleto();
    }
}

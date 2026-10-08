package parroquia;

public class Sacerdote extends Persona {

    public Sacerdote(String nombreCompleto, String dni, String telefono) {
        super(nombreCompleto, dni, telefono);
    }

    @Override
    public String describir() {
        return "Sacerdote: " + getNombreCompleto();
    }
}
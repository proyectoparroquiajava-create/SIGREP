package parroquia;

public class Feligres extends Persona {

    private final String direccion;

    public Feligres(String nombreCompleto, String dni, String telefono, String direccion) {
        super(nombreCompleto, dni, telefono);
        if (direccion == null || direccion.trim().length() < 5) {
            throw new DatosInvalidosException("La direccion es demasiado corta o esta vacia.");
        }
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String describir() {
        return "Feligres: " + getNombreCompleto();
    }
}

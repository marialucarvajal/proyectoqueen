public abstract class Carta {

    protected String descripcion;

    public Carta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract String getTipo();
}

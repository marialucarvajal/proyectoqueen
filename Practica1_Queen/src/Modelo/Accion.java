package Modelo;

public class Accion {
    private String tipo;
    private String nombre;

    public Accion(String tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }
}


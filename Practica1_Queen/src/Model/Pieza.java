package Model;

public abstract class Pieza {

    protected String color;
    protected String tipo;

    public Pieza(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getTipo() {
        return tipo;
    }

    public abstract String getSimbolo();
}


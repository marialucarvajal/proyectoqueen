package Modelo.piezas;

public class Guardiana extends Pieza {

    public Guardiana(String color) {
        super(color);
        this.tipo = "Guardiana";
    }

    @Override
    public String getSimbolo() {
        if (color.equalsIgnoreCase("rojo")) {
            return " Gr";
        } else if (color.equalsIgnoreCase("azul")) {
            return " Ga";
        } else {
            return " G?";
        }
    }
}

public class Reina extends Pieza {

    public Reina(String color) {
        super(color);
        this.tipo = "Reina";
    }

    @Override
    public String getSimbolo() {
        return color.equalsIgnoreCase("rojo") ? " Qr" : " Qa";
    }
}
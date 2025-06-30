public class Casilla {

    private String color;
    private Pieza pieza;

    public Casilla(String color){
        this.color = color;
        this.pieza = null;
    }

    public String getColor() {
        return color;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void colocarPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public void eliminarPieza() {
        this.pieza = null;
    }

    public String mostrar() {
        if (pieza != null) {
            return pieza.getSimbolo();
        }

        return switch (color) {
            case "rojo" -> " R ";
            case "azul" -> " A ";
            case "blanco" -> " . ";
            case "negro" -> " # ";
            default -> " ? ";
        };
    }
}


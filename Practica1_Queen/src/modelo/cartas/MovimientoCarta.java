package modelo.cartas;

import modelo.Carta;

public class MovimientoCarta extends Carta {

    public MovimientoCarta(String descripcion) {
        super(descripcion);
    }

    @Override
    public String getTipo() {
        return "Movimiento";
    }
}

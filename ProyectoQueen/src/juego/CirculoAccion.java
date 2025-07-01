package juego;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CirculoAccion {
    private int[] posicionesJ1 = {1, 2, 3};
    private int[] posicionesJ2 = {1, 2, 3};

    private static final List<String> ACCIONES = Arrays.asList(
            "INICIO", "TOMAR_CARTA", "MOVIMIENTO", "ATAQUE",
            "ROTACION", "MOVIMIENTO", "TOMAR_CARTA", "TOMAR_CARTA",
            "ATAQUE", "ATAQUE", "ROTACION", "MOVIMIENTO",
            "TOMAR_CARTA", "FIN"
    );

    public List<String> obtenerAccionesDisponibles(Jugador jugador) {
        List<String> acciones = new ArrayList<>();

        acciones.add("TOMAR_CARTA");

        if(!jugador.getCartasMovimiento().isEmpty()) {
            acciones.add("MOVIMIENTO");
        }

        if(!jugador.getCartasAtaque().isEmpty()) {
            acciones.add("ATAQUE");
        }

        return acciones;
    }

    public void usarAccion(Jugador jugador, String accion) {
        int[] posiciones = jugador.getColor().equals("Rojo") ? posicionesJ1 : posicionesJ2;

        for(int i = 0; i < posiciones.length; i++) {
            if(ACCIONES.get(posiciones[i]).equals(accion)) {
                posiciones[i] = (posiciones[i] + 1) % ACCIONES.size();
                break;
            }
        }
    }
}


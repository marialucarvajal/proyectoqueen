package juego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CirculoAccion {
    private static final List<String> ACCIONES = Arrays.asList(
            "INICIO", "TOMAR_CARTA", "MOVIMIENTO", "ATAQUE",
            "ROTACION", "MOVIMIENTO", "TOMAR_CARTA", "TOMAR_CARTA",
            "ATAQUE", "ATAQUE", "ROTACION", "MOVIMIENTO",
            "TOMAR_CARTA", "FIN"
    );

    private int[] posicionesFichasJ1 = {0, 0, 0};
    private int[] posicionesFichasJ2 = {0, 0, 0};

    public String obtenerAccion(int posicion) {
        return ACCIONES.get(posicion % ACCIONES.size());
    }

    public int siguientePosicion(int posicionActual, int pasos) {
        return (posicionActual + pasos) % ACCIONES.size();
    }

    public List<String> obtenerAccionesDisponibles(Jugador jugador) {
        List<String> disponibles = new ArrayList<>();
        int[] fichas = (jugador.getColor().equals("Rojo")) ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int posicion : fichas) {
            String accion = obtenerAccion(posicion + 1);
            if (!disponibles.contains(accion)) {
                disponibles.add(accion);
            }
        }
        return disponibles;
    }

    public void moverFicha(Jugador jugador, int fichaIndex, int pasos) {
        if (jugador.getColor().equals("Rojo")) {
            posicionesFichasJ1[fichaIndex] = siguientePosicion(posicionesFichasJ1[fichaIndex], pasos);
        } else {
            posicionesFichasJ2[fichaIndex] = siguientePosicion(posicionesFichasJ2[fichaIndex], pasos);
        }
    }
}

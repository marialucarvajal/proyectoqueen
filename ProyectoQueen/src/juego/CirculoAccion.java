package juego;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CirculoAccion {

    public CirculoAccion() {
        posicionesFichasJ1 = new int[]{0, 4, 8};
        posicionesFichasJ2 = new int[]{0, 4, 8};
    }

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
            for (int i = 1; i <= 2; i++) {
                String accion = obtenerAccion(posicion + i);
                if (!disponibles.contains(accion) && !accion.equals("INICIO") && !accion.equals("FIN")) {
                    disponibles.add(accion);
                }
            }
        }
        return disponibles;
    }

    public void moverFicha(Jugador jugador, int fichaIndex, int pasos) {
        if (jugador.getColor().equals("Rojo") && fichaIndex < posicionesFichasJ1.length) {
            posicionesFichasJ1[fichaIndex] = siguientePosicion(posicionesFichasJ1[fichaIndex], pasos);
        } else if (fichaIndex < posicionesFichasJ2.length) {
            posicionesFichasJ2[fichaIndex] = siguientePosicion(posicionesFichasJ2[fichaIndex], pasos);
        }
    }
}


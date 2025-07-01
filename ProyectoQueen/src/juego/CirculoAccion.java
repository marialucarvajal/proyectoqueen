package juego;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CirculoAccion {
    private static final List<String> ACCIONES = Arrays.asList(
            "INICIO", "TOMAR_CARTA", "MOVIMIENTO", "ATAQUE",
            "ROTACION", "MOVIMIENTO", "TOMAR_CARTA", "TOMAR_CARTA",
            "ATAQUE", "ATAQUE", "ROTACION", "MOVIMIENTO",
            "TOMAR_CARTA", "FIN"
    );

    private int[] posicionesFichasJ1 = {0, 0, 0};
    private int[] posicionesFichasJ2 = {0, 0, 0};

    public List<String> obtenerAccionesDisponibles(Jugador jugador) {
        List<String> disponibles = new ArrayList<>();
        int[] fichas = (jugador.getColor().equals("Rojo")) ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int posicion : fichas) {
            int nuevaPos = siguientePosicion(posicion, 1);
            String accion = obtenerAccion(nuevaPos);
            if (!disponibles.contains(accion)) {
                disponibles.add(accion);
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

    private String obtenerAccion(int posicion) {
        return ACCIONES.get(posicion % ACCIONES.size());
    }

    private int siguientePosicion(int posicionActual, int pasos) {
        return (posicionActual + pasos) % ACCIONES.size();
    }

    public boolean puedeAvanzar(Jugador jugador, int pasos) {
        int[] fichas = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int posicion : fichas) {
            int nuevaPos = siguientePosicion(posicion, pasos);
            String accion = obtenerAccion(nuevaPos);

            if (accion.equals("FIN")) {
                int posPrevia = siguientePosicion(nuevaPos, -1);
                String accionPrevia = obtenerAccion(posPrevia);
                if (!accionPrevia.equals("TOMAR_CARTA")) {
                    return false;
                }
            }
        }
        return true;
    }
}


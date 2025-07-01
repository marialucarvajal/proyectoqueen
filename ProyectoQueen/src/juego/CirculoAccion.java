package juego;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CirculoAccion() {
    private int fichasEnMovimiento = 0;
    private int fichasEnAtaque = 0;
    private int fichasEnTomarCarta = 0;
    private int fichasEnRotacion = 0;

    private static final List<String> ACCIONES = Arrays.asList(
            "Inicio", "Tomar Carta", "Movimiento", "Ataque",
            "Rotación", "Movimiento", "Tomar Carta", "Tomar Carta",
            "Ataque", "Ataque", "Rotación", "Movimiento",
            "Tomar Carta", "Fin"
    );

    private int[] posicionesFichasJ1 = {0, 0, 0};
    private int[] posicionesFichasJ2 = {0, 0, 0};

    public CirculoAccion() {
        for (int i = 0; i < 3; i++) {
            moverFicha(new Jugador("Rojo"), i, 0);
            moverFicha(new Jugador("Azul"), i, 0);
        }
    }

    public List<String> obtenerAccionesDisponibles(Jugador jugador) {
        List<String> acciones = new ArrayList<>();
        actualizarContadoresFichas(jugador);

        if (fichasEnMovimiento > 0 && !jugador.getCartasMovimiento().isEmpty()) {
            acciones.add("MOVIMIENTO");
        }
        if (fichasEnAtaque > 0 && !jugador.getCartasAtaque().isEmpty()) {
            acciones.add("ATACAR");
        }
        if (fichasEnTomarCarta > 0) {
            acciones.add("TOMAR_CARTA");
        }
        if (fichasEnRotacion > 0) {
            acciones.add("ROTAR");
        }

        return acciones;
    }

    private void actualizarContadoresFichas(Jugador jugador) {
        fichasEnMovimiento = 0;
        fichasEnAtaque = 0;
        fichasEnTomarCarta = 0;
        fichasEnRotacion = 0;

        int[] posiciones = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int pos : posiciones) {
            String accion = ACCIONES.get(pos);
            switch(accion) {
                case "MOVIMIENTO": fichasEnMovimiento++; break;
                case "ATAQUE": fichasEnAtaque++; break;
                case "TOMAR_CARTA": fichasEnTomarCarta++; break;
                case "ROTACION": fichasEnRotacion++; break;
            }
        }
    }

    public void moverFicha(Jugador jugador, int fichaIndex, int pasos) {
        int[] fichas = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        if (fichaIndex >= 0 && fichaIndex < fichas.length) {
            int nuevaPos = siguientePosicion(fichas[fichaIndex], pasos);

            if (ACCIONES.get(nuevaPos).equals("FIN")) {
                int posPrevia = siguientePosicion(nuevaPos, -1);
                if (!ACCIONES.get(posPrevia).equals("TOMAR_CARTA")) {
                    throw new IllegalStateException("Solo se puede llegar al FIN desde TOMAR_CARTA");
                }
            }

            fichas[fichaIndex] = nuevaPos;
            actualizarContadoresFichas(jugador);
        }
    }

    private int siguientePosicion(int posicionActual, int pasos) {
        int nuevaPos = (posicionActual + pasos) % ACCIONES.size();
        return nuevaPos < 0 ? nuevaPos + ACCIONES.size() : nuevaPos;
    }

    public void usarAccion(Jugador jugador, String accion) {
        int[] fichas = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int i = 0; i < fichas.length; i++) {
            if (ACCIONES.get(fichas[i]).equals(accion)) {
                moverFicha(jugador, i, 1);
                return;
            }
        }
        throw new IllegalStateException("No hay fichas en posición para: " + accion);
    }

    public boolean puedeRotar(Jugador jugador) {
        actualizarContadoresFichas(jugador);
        return fichasEnRotacion > 0;
    }

    public String obtenerEstado(Jugador jugador) {
        int[] posiciones = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;
        StringBuilder sb = new StringBuilder();

        sb.append("Posiciones de fichas (").append(jugador.getColor()).append("):\n");
        for (int i = 0; i < posiciones.length; i++) {
            sb.append("Ficha ").append(i+1).append(": ")
                    .append(ACCIONES.get(posiciones[i])).append("\n");
        }

        return sb.toString();
    }
}


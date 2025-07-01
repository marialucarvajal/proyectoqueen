package juego;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CirculoAccion {
    private int[] posicionesFichasJ1;
    private int[] posicionesFichasJ2;
    private int fichasEnTomarCarta;
    private int fichasEnMovimiento;
    private int fichasEnAtaque;
    private int fichasEnRotacion;

    private static final List<String> ACCIONES = Arrays.asList(
            "INICIO", "TOMAR_CARTA", "MOVIMIENTO", "ATAQUE",
            "ROTACION", "MOVIMIENTO", "TOMAR_CARTA", "TOMAR_CARTA",
            "ATAQUE", "ATAQUE", "ROTACION", "MOVIMIENTO",
            "TOMAR_CARTA", "FIN"
    );

    public CirculoAccion() {
        posicionesFichasJ1 = new int[]{1, 2, 3};
        posicionesFichasJ2 = new int[]{1, 2, 3};
        actualizarContadoresFichas(new Jugador("Rojo"));
        actualizarContadoresFichas(new Jugador("Azul"));
    }

    private void actualizarContadoresFichas(Jugador jugador) {
        fichasEnTomarCarta = 0;
        fichasEnMovimiento = 0;
        fichasEnAtaque = 0;
        fichasEnRotacion = 0;

        int[] posiciones = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int pos : posiciones) {
            String accion = ACCIONES.get(pos);
            switch(accion) {
                case "TOMAR_CARTA": fichasEnTomarCarta++; break;
                case "MOVIMIENTO": fichasEnMovimiento++; break;
                case "ATAQUE": fichasEnAtaque++; break;
                case "ROTACION": fichasEnRotacion++; break;
            }
        }
    }

    public List<String> obtenerAccionesDisponibles(Jugador jugador) {
        List<String> acciones = new ArrayList<>();
        actualizarContadoresFichas(jugador);

        if (fichasEnTomarCarta > 0) {
            acciones.add("TOMAR_CARTA");
        }
        if (fichasEnMovimiento > 0 && !jugador.getCartasMovimiento().isEmpty()) {
            acciones.add("MOVIMIENTO");
        }
        if (fichasEnAtaque > 0 && !jugador.getCartasAtaque().isEmpty()) {
            acciones.add("ATAQUE");
        }
        if (fichasEnRotacion > 0) {
            acciones.add("ROTACION"); 
        }

        return acciones;
    }
}

    public void usarAccion(Jugador jugador, String accion) {
        int[] posiciones = jugador.getColor().equals("Rojo") ? posicionesFichasJ1 : posicionesFichasJ2;

        for (int i = 0; i < posiciones.length; i++) {
            if (ACCIONES.get(posiciones[i]).equals(accion)) {
                posiciones[i] = (posiciones[i] + 1) % ACCIONES.size();
                break;
            }
        }
        actualizarContadoresFichas(jugador);
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


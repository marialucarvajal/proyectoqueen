package ui;

import juego.*;

import java.util.List;

public class Consola {

    public static void mostrarTablero(Tablero tablero) {
        Imprimir.mensaje("\n   A B C D E");
        for(int i = 0; i < 6; i++) {
            StringBuilder fila = new StringBuilder((i+1) + " ");
            for(int j = 0; j < 5; j++) {
                Pieza p = tablero.obtenerPieza(i, j);
                fila.append(" ").append(p == null ? "." :
                        p.getJugador().getColor().equals("Rojo") ?
                                (p.esReina() ? "Q" : "G") :
                                (p.esReina() ? "q" : "g"));
            }
            Imprimir.mensaje(fila.toString());
        }
    }

    public static void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\n--- Turno: Jugador " + jugador.getColor() + " ---");
        System.out.println("Cartas: " + jugador.getCartasCount());
    }

    public static void mostrarAccionesDisponibles(List<String> acciones) {
        Imprimir.mensaje("\nAcciones posibles:");
        for (int i = 0; i < acciones.size(); i++) {
            Imprimir.mensaje((i+1) + ". " + acciones.get(i));
        }
        Imprimir.mensaje("0. SALIR");
    }
}

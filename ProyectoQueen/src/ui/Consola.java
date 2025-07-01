package ui;

import juego.*;

import java.util.List;

public class Consola {

    public static void mostrarTablero(Tablero tablero) {
        System.out.println("\n   A B C D E");
        for (int fila = 0; fila < 6; fila++) {
            System.out.print((fila + 1) + " ");
            for (int col = 0; col < 5; col++) {
                Pieza pieza = tablero.obtenerPieza(fila, col);
                if (pieza == null) {
                    System.out.print(". ");
                } else {
                    char simbolo = pieza.esReina() ? 'Q' : 'G';
                    String color = pieza.getJugador().getColor().equals("Rojo") ? "\u001B[31m" : "\u001B[34m";
                    System.out.print(color + simbolo + "\u001B[0m ");
                }
            }
            System.out.println();
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

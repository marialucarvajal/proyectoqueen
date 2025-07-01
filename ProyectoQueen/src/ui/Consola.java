package ui;

import juego.*;
import java.util.List;

public class Consola {
    private static final String RESET = "\u001B[0m";
    private static final String RED_TEXT = "\u001B[31m";
    private static final String BLUE_TEXT = "\u001B[34m";
    private static final String RED_BG = "\u001B[41m";
    private static final String BLUE_BG = "\u001B[44m";
    private static final String BLACK_BG = "\u001B[40m";
    private static final String WHITE_BG = "\u001B[47m";
    private static final String BOLD = "\u001B[1m";
    private static final String YELLOW_TEXT = "\u001B[33m";

    public static void mostrarTablero(Tablero tablero) {
        System.out.println("\n   " + BOLD + "A   B   C   D   E" + RESET);

        for (int fila = 0; fila < 6; fila++) {
            System.out.print(BOLD + (fila+1) + " " + RESET);

            for (int col = 0; col < 5; col++) {
                Pieza p = tablero.obtenerPieza(fila, col);
                String simbolo = " ";
                String colorTexto = "";
                String colorFondo = obtenerColorFondo(tablero, fila, col);

                if (p != null) {
                    simbolo = p.esReina() ? "♕" : "♙";
                    colorTexto = p.getJugador().getColor().equals("Rojo") ? RED_TEXT : BLUE_TEXT;
                }

                System.out.print(colorFondo + colorTexto + " " + simbolo + " " + RESET);
            }
            System.out.println();
        }
    }

    private static String obtenerColorFondo(Tablero tablero, int fila, int col) {
        switch(tablero.obtenerTipoCasilla(fila, col)) {
            case ROJA: return RED_BG;
            case AZUL: return BLUE_BG;
            case NEGRA: return BLACK_BG;
            default: return WHITE_BG;
        }
    }

    public static void mostrarEstadoJugador(Jugador jugador) {
        String color = jugador.getColor().equals("Rojo") ? RED_TEXT : BLUE_TEXT;
        String separador = "════════════════════════";

        System.out.println("\n" + separador);
        System.out.println(BOLD + " TURNO: Jugador " + color + jugador.getColor() + RESET + BOLD + " " + RESET);
        System.out.println(separador);
        System.out.println(" Cartas Movimiento: " + YELLOW_TEXT + jugador.getCartasMovimiento().size() + RESET);
        System.out.println(" Cartas Ataque:    " + YELLOW_TEXT + jugador.getCartasAtaque().size() + RESET);
        System.out.println(separador);
    }

    public static void mostrarAccionesDisponibles(List<String> acciones) {
        System.out.println("\n" + BOLD + "ACCIONES DISPONIBLES:" + RESET);
        for (int i = 0; i < acciones.size(); i++) {
            System.out.printf(" %d. %s%n", i+1, acciones.get(i));
        }
        System.out.println(" 0. SALIR\n");
    }

    public static void mostrarMensajeEspecial(String mensaje) {
        String marco = "✧･ﾟ: *✧･ﾟ:* ";
        System.out.println("\n" + YELLOW_TEXT + BOLD + marco + mensaje + marco + RESET);
    }

    public static void mostrarError(String mensaje) {
        System.out.println(RED_TEXT + "⚠ " + mensaje + RESET);
    }
}
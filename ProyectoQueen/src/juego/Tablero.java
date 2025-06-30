package juego;

import juego.Jugador;
import juego.Pieza;

import java.util.Arrays;

public class Tablero {
    private Pieza[][] casillas;
    private final int FILAS = 6;
    private final int COLUMNAS = 5;

    public Tablero() {
        casillas = new Pieza[FILAS][COLUMNAS];
    }

    public void colocarPiezasIniciales(Jugador jugador1, Jugador jugador2) {

        for (int i = 0; i < FILAS; i++) {
            Arrays.fill(casillas[i], null);
        }

        casillas[0][0] = new Pieza(jugador1, true);
        casillas[0][1] = new Pieza(jugador1, false);
        casillas[1][0] = new Pieza(jugador1, false);

        casillas[5][4] = new Pieza(jugador2, true);
        casillas[5][3] = new Pieza(jugador2, false);
        casillas[4][4] = new Pieza(jugador2, false);
    }

    public boolean moverPieza(int filaOrigen, int columnaOrigen,
                              int filaDestino, int columnaDestino) {

        if (!esPosicionValida(filaOrigen, columnaOrigen) ||
                !esPosicionValida(filaDestino, columnaDestino)) {
            return false;
        }

        Pieza pieza = casillas[filaOrigen][columnaOrigen];
        if (pieza == null) {
            return false;
        }

        if (casillas[filaDestino][columnaDestino] != null) {
            return false;
        }

        int diffFila = Math.abs(filaDestino - filaOrigen);
        int diffCol = Math.abs(columnaDestino - columnaOrigen);

        boolean movimientoValido;
        if (pieza.esReina()) {
            movimientoValido = (diffFila <= 1 && diffCol <= 1);
        } else {
            movimientoValido = (diffFila == 1 && diffCol == 0) ||
                    (diffCol == 1 && diffFila == 0);
        }

        if (movimientoValido) {
            casillas[filaDestino][columnaDestino] = pieza;
            casillas[filaOrigen][columnaOrigen] = null;
            return true;
        }

        return false;
    }

    private boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < 6 && columna >= 0 && columna < 5;
    }

    public Pieza obtenerPieza(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return null;
        }
        return casillas[fila][columna];
    }
}

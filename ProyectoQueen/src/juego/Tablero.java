package juego;

import ui.Imprimir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tablero {
    private Pieza[][] casillas;
    private TipoCasilla[][] tiposCasillas;
    private final int FILAS = 6;
    private final int COLUMNAS = 5;

    public Tablero() {
        casillas = new Pieza[FILAS][COLUMNAS];
        tiposCasillas = new TipoCasilla[FILAS][COLUMNAS];
        inicializarTiposCasillas();
    }

    private void inicializarTiposCasillas() {
        tiposCasillas[0][0] = TipoCasilla.ROJA;
        tiposCasillas[5][4] = TipoCasilla.AZUL;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tiposCasillas[i][j] == null) {
                    tiposCasillas[i][j] = (i + j) % 2 == 0 ? TipoCasilla.BLANCA : TipoCasilla.NEGRA;
                }
            }
        }
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

    public boolean moverPieza(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        if (!esPosicionValida(filaOrigen, colOrigen) || !esPosicionValida(filaDestino, colDestino)) {
            return false;
        }

        Pieza pieza = casillas[filaOrigen][colOrigen];
        if (pieza == null) {
            return false;
        }

        Pieza piezaDestino = casillas[filaDestino][colDestino];
        if (piezaDestino != null && piezaDestino.getJugador().equals(pieza.getJugador())) {
            return false;
        }

        if (pieza.esReina() &&
                ((pieza.getJugador().getColor().equals("Rojo") && tiposCasillas[filaOrigen][colOrigen] == TipoCasilla.ROJA) ||
                        (pieza.getJugador().getColor().equals("Azul") && tiposCasillas[filaOrigen][colOrigen] == TipoCasilla.AZUL))) {
            Imprimir.mensaje("¡Reina moviéndose desde su casilla especial!");
        }

        casillas[filaDestino][colDestino] = pieza;
        casillas[filaOrigen][colOrigen] = null;
        return true;
    }

    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    public Pieza obtenerPieza(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return null;
        }
        return casillas[fila][columna];
    }

    public TipoCasilla obtenerTipoCasilla(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return null;
        }
        return tiposCasillas[fila][columna];
    }

    public boolean piezaPerteneceAJugador(int fila, int columna, Jugador jugador) {
        Pieza pieza = obtenerPieza(fila, columna);
        return pieza != null && pieza.getJugador().equals(jugador);
    }

    public boolean moverPiezaDistancia(int filaOrigen, int colOrigen, int filaDestino, int colDestino, int distancia) {
        int diffFila = Math.abs(filaDestino - filaOrigen);
        int diffCol = Math.abs(colDestino - colOrigen);

        if ((diffFila == 0 || diffCol == 0) && (diffFila <= distancia && diffCol <= distancia)) {
            int pasoFila = Integer.compare(filaDestino, filaOrigen);
            int pasoCol = Integer.compare(colDestino, colOrigen);

            int f = filaOrigen + pasoFila;
            int c = colOrigen + pasoCol;
            while (f != filaDestino || c != colDestino) {
                if (obtenerPieza(f, c) != null) {
                    return false;
                }
                f += pasoFila;
                c += pasoCol;
            }

            return moverPieza(filaOrigen, colOrigen, filaDestino, colDestino);
        }
        return false;
    }

    public boolean moverPiezaEnL(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        int diffFila = Math.abs(filaDestino - filaOrigen);
        int diffCol = Math.abs(colDestino - colOrigen);

        if ((diffFila == 2 && diffCol == 1) || (diffFila == 1 && diffCol == 2)) {
            return moverPieza(filaOrigen, colOrigen, filaDestino, colDestino);
        }
        return false;
    }

    public List<Pieza> obtenerPiezasJugador(Jugador jugador) {
        List<Pieza> piezas = new ArrayList<>();
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = obtenerPieza(i, j);
                if (p != null && p.getJugador().equals(jugador)) {
                    piezas.add(p);
                }
            }
        }
        return piezas;
    }

    public boolean eliminarPieza(int fila, int columna) {
        if (!esPosicionValida(fila, columna) || casillas[fila][columna] == null) {
            return false;
        }
        casillas[fila][columna] = null;
        return true;
    }

    public boolean estaEnCasillaEspecial(int fila, int columna) {
        if (!esPosicionValida(fila, columna)) {
            return false;
        }
        Pieza pieza = obtenerPieza(fila, columna);
        if (pieza == null || !pieza.esReina()) {
            return false;
        }

        TipoCasilla tipo = tiposCasillas[fila][columna];
        return (pieza.getJugador().getColor().equals("Rojo") && tipo == TipoCasilla.ROJA) ||
                (pieza.getJugador().getColor().equals("Azul") && tipo == TipoCasilla.AZUL);
    }
}


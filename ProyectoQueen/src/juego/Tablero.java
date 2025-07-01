package juego;

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
        inicializarTiposCasillas();
    }

    private void inicializarTiposCasillas() {
        tiposCasillas = new TipoCasilla[FILAS][COLUMNAS];

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

    public boolean moverPieza(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        if (!esPosicionValida(filaOrigen, columnaOrigen) || !esPosicionValida(filaDestino, columnaDestino)) {
            return false;
        }

        Pieza pieza = casillas[filaOrigen][columnaOrigen];
        if (pieza == null) return false;

        int diffFila = Math.abs(filaDestino - filaOrigen);
        int diffCol = Math.abs(columnaDestino - columnaOrigen);

        if (diffFila <= 1 && diffCol <= 1 && (diffFila + diffCol) > 0) {
            casillas[filaDestino][columnaDestino] = pieza;
            casillas[filaOrigen][columnaOrigen] = null;
            return true;
        }
        return false;
    }

    private boolean esPosicionValida(int fila, int columna) {
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
            // Verificar que no haya piezas en el camino
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
}


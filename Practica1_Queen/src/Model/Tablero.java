package Model;

public class Tablero {
    private Casilla[][] casillas;

    public Tablero() {
        casillas = new Casilla[6][5];
        inicializarCasillas();
        colocarPiezasIniciales();
    }

    private void inicializarCasillas() {
        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 5; col++) {
                if (fila == 0 && col == 2) {
                    casillas[fila][col] = new Casilla("rojo");
                } else if (fila == 5 && col == 2) {
                    casillas[fila][col] = new Casilla("azul");
                } else if ((fila + col) % 2 == 0) {
                    casillas[fila][col] = new Casilla("blanco");
                } else {
                    casillas[fila][col] = new Casilla("negro");
                }
            }
        }
    }

    private void colocarPiezasIniciales() {

        casillas[0][2].colocarPieza(new Reina("rojo"));
        casillas[0][1].colocarPieza(new Guardiana("rojo"));
        casillas[0][3].colocarPieza(new Guardiana("rojo"));

        casillas[5][2].colocarPieza(new Reina("azul"));
        casillas[5][1].colocarPieza(new Guardiana("azul"));
        casillas[5][3].colocarPieza(new Guardiana("azul"));
    }

    public void imprimirTablero() {
        System.out.println("    1   2   3   4   5");
        for (int fila = 0; fila < 6; fila++) {
            char letraFila = (char) ('A' + fila);
            System.out.print(letraFila + " ");
            for (int col = 0; col < 5; col++) {
                System.out.print(" " + casillas[fila][col].mostrar());
            }
            System.out.println();
        }
    }

    public int[] convertirCoordenada(String entrada) {
        if (entrada.length() != 2) return null;

        char filaChar = Character.toUpperCase(entrada.charAt(0));
        int fila = filaChar - 'A';
        int columna = Character.getNumericValue(entrada.charAt(1)) - 1;

        if (fila >= 0 && fila < 6 && columna >= 0 && columna < 5) {
            return new int[]{fila, columna};
        } else {
            return null;
        }
    }

    public boolean moverPieza(String origen, String destino, String colorJugador) {
        int[] origenCoord = convertirCoordenada(origen);
        int[] destinoCoord = convertirCoordenada(destino);

        if (origen.equals(destino)) {
            System.out.println("Origen y destino son iguales.");
            return false;
        }

        if (origenCoord == null || destinoCoord == null) {
            System.out.println("Coordenadas inválidas.");
            return false;
        }

        int filaO = origenCoord[0];
        int colO = origenCoord[1];
        int filaD = destinoCoord[0];
        int colD = destinoCoord[1];

        Casilla casillaOrigen = casillas[filaO][colO];
        Casilla casillaDestino = casillas[filaD][colD];

        if (casillaOrigen.getPieza() == null) {
            System.out.println("No hay pieza en la casilla de origen.");
            return false;
        }

        if (!casillaOrigen.getPieza().getColor().equals(colorJugador)) {
            System.out.println("Esa pieza no te pertenece.");
            return false;
        }

        if (casillaDestino.getPieza() != null) {
            System.out.println("Ya hay una pieza en la casilla de destino.");
            return false;
        }

        int difFila = Math.abs(filaD - filaO);
        int difCol = Math.abs(colD - colO);
        if (difFila <= 1 && difCol <= 1) {
            casillaDestino.colocarPieza(casillaOrigen.getPieza());
            casillaOrigen.eliminarPieza();
            return true;
        } else {
            System.out.println("Movimiento inválido: solo se puede mover 1 espacio.");
            return false;
        }
    }

    public Pieza obtenerPieza(String coordenada) {
        int[] coord = convertirCoordenada(coordenada);
        if (coord == null) return null;
        return casillas[coord[0]][coord[1]].getPieza();
    }

    public boolean moverPiezaDoble(String origen, String destino, String colorJugador) {
        int[] o = convertirCoordenada(origen);
        int[] d = convertirCoordenada(destino);
        if (o == null || d == null) return false;

        boolean mismoFila = (o[0] == d[0]);
        boolean mismaCol = (o[1] == d[1]);
        if (!mismoFila && !mismaCol) {
            System.out.println("Movimiento doble debe ser en línea recta.");
            return false;
        }

        int distancia = Math.abs(o[0] - d[0]) + Math.abs(o[1] - d[1]);
        if (distancia != 2) {
            System.out.println("Movimiento doble debe ser exactamente 2 espacios.");
            return false;
        }

        Casilla origenC = casillas[o[0]][o[1]];
        Casilla destinoC = casillas[d[0]][d[1]];
        if (origenC.getPieza() == null || !origenC.getPieza().getColor().equals(colorJugador)) {
            System.out.println("Pieza inválida o no te pertenece.");
            return false;
        }
        if (destinoC.getPieza() != null) {
            System.out.println("Casilla destino ocupada.");
            return false;
        }

        destinoC.colocarPieza(origenC.getPieza());
        origenC.eliminarPieza();
        return true;
    }

    public boolean eliminarPiezaEnemiga(String coord, String colorJugador) {
        int[] c = convertirCoordenada(coord);
        if (c == null) return false;

        Casilla objetivo = casillas[c[0]][c[1]];
        if (objetivo.getPieza() != null && !objetivo.getPieza().getColor().equals(colorJugador)) {
            objetivo.eliminarPieza();
            return true;
        }

        System.out.println("No hay enemigo en esa casilla.");
        return false;
    }

    public boolean hayReinaViva(String color) {
        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 5; col++) {
                Pieza pieza = casillas[fila][col].getPieza();
                if (pieza != null && pieza instanceof Reina && pieza.getColor().equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean esMovimientoValido(String origen, String destino, boolean esMovimientoDoble) {
        int[] coordOrigen = convertirCoordenada(origen);
        int[] coordDestino = convertirCoordenada(destino);

        if (coordOrigen == null || coordDestino == null) return false;

        int difFilas = Math.abs(coordDestino[0] - coordOrigen[0]);
        int difCols = Math.abs(coordDestino[1] - coordOrigen[1]);

        return esMovimientoDoble ?
                (difFilas == 2 && difCols == 0) || (difFilas == 0 && difCols == 2) :
                difFilas <= 1 && difCols <= 1;
    }

}
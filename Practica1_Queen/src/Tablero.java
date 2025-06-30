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

    public boolean moverPieza(String origen, String destino) {
        int[] origenCoord = convertirCoordenada(origen);
        int[] destinoCoord = convertirCoordenada(destino);

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
}
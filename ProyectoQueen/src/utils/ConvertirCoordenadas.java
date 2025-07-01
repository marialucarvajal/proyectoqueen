package utils;

public class ConvertirCoordenadas {
    public static int[] desdeNotacionTablero(String input) {
        try {
            input = input.toUpperCase().trim();

            if (input.length() != 2) {
                throw new IllegalArgumentException("Formato incorrecto");
            }

            char columnaChar = input.charAt(0);
            char filaChar = input.charAt(1);

            if (columnaChar < 'A' || columnaChar > 'E') {
                throw new IllegalArgumentException("Columna debe ser entre A y E");
            }

            if (filaChar < '1' || filaChar > '6') {
                throw new IllegalArgumentException("Fila debe ser entre 1 y 6");
            }

            int columna = columnaChar - 'A';
            int fila = 6 - (filaChar - '0');

            return new int[]{fila, columna};

        } catch (Exception e) {
            System.out.println("Error en coordenadas: " + e.getMessage());
            System.out.println("Use formato LetraNúmero (ej. A1, B3, E5)");
            return null;
        }
    }

    public static String aNotacionTablero(int fila, int columna) {
        if (fila < 0 || fila > 5 || columna < 0 || columna > 4) {
            return "??";
        }
        char columnaChar = (char) ('A' + columna);
        int filaNum = 6 - fila;
        return "" + columnaChar + filaNum;
    }
}
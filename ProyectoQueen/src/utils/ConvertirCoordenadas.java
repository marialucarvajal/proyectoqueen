package utils;

public class ConvertirCoordenadas {
    public static int[] desdeNotacionTablero(String input) {
        try {
            input = input.toUpperCase().trim();

            if (input.length() < 2) {
                throw new IllegalArgumentException("Formato incorrecto");
            }

            char letra = input.charAt(0);
            String numeroStr = input.substring(1);

            if (letra < 'A' || letra > 'F') {
                throw new IllegalArgumentException("Letra debe ser entre A y F");
            }

            int fila = letra - 'A';
            int columna = Integer.parseInt(numeroStr) - 1;

            if (columna < 0 || columna > 4) {
                throw new IllegalArgumentException("Número debe ser entre 1 y 5");
            }

            return new int[]{fila, columna};

        } catch (Exception e) {
            System.out.println("Error en coordenadas: " + e.getMessage());
            System.out.println("Use formato LetraNúmero (ej. A1, B3, F5)");
            return null;
        }
    }

    public static String aNotacionTablero(int fila, int columna) {
        char letra = (char) ('A' + fila);
        return letra + String.valueOf(columna + 1);
    }
}
package utils;

public class ConvertirCoordenadas {
    public static int[] desdeNotacionTablero(String coordenada) {
        if (coordenada == null || coordenada.length() < 2) return null;

        try {
            char letra = Character.toUpperCase(coordenada.charAt(0));
            int fila = letra - 'A';  // A=0, B=1, ..., F=5
            int columna = Integer.parseInt(coordenada.substring(1)) - 1; // 1=0, 2=1, ..., 5=4

            if (fila < 0 || fila > 5 || columna < 0 || columna > 4) {
                return null;
            }
            return new int[]{fila, columna};
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

package utils;

import java.util.Scanner;

public class InputAyuda {
    private static final Scanner scanner = new Scanner(System.in);

    public static String obtenerInput(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public static int[] obtenerCoordenadas(String mensaje) {
        System.out.print(mensaje);
        String input = scanner.nextLine().trim();
        return ConvertirCoordenadas.desdeNotacionTablero(input);
    }
}

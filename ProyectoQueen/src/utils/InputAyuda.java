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

    public static int obtenerEntero(String mensaje, int min, int max) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Error: Ingrese un número entre " + min + " y " + max);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido");
            }
        }
    }
}

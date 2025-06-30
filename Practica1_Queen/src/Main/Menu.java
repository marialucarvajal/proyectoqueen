package Main;

import java.util.Scanner;

public class Menu {

    private Scanner scanner;

    public Menu(){
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu(){
        int opcion = 0;

        while(opcion != 2){
            System.out.println("QUEEN ON THE FIELD");
            System.out.println("1. Jugar");
            System.out.println("2. Salir");

            String escoger = scanner.nextLine().trim();

            if(escoger.equals("2")){
                System.out.println("Saliendo del juego...");
                opcion = 2;
            } else if (escoger.equals("1")){
                Juego juego = new Juego();
                juego.iniciarJuego();
            } else {
                System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }
}

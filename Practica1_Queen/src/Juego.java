import java.util.Scanner;

public class Juego {

    private boolean ejecucion;
    private Scanner scanner;

    public Juego() {
        this.ejecucion = true;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarJuego() {
        while (ejecucion) {
            mostrarMenu();
            String option = scanner.nextLine().trim().toLowerCase();

            switch (option) {
                case "1":
                    jugar();
                    break;
                case "2":
                    System.out.println("Saliendo del juego...");
                    ejecucion = false;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor intente de nuevo.");
            }
        }
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\nQUEEN ON THE FIELD");
        System.out.println("1. Jugar");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void jugar() {
        Tablero tablero = new Tablero();
        CirculoAcciones circulo = new CirculoAcciones();
        MazoCartas mazoMov = new MazoCartas("Movimiento");
        MazoCartas mazoAtk = new MazoCartas("Ataque");

        Jugador jugador1 = new Jugador("Jugador 1", "rojo");
        Jugador jugador2 = new Jugador("Jugador 2", "azul");

        boolean jugando = true;
        int turno = 1;

        while (jugando) {
            Jugador actual = turno % 2 == 1 ? jugador1 : jugador2;
            System.out.println("\nTurno de " + actual.getNombre() + " (" + actual.getColor() + ")");
            circulo.imprimirCirculoConFichas(actual.getFichasAccion());
            System.out.println("Cartas en mano: " + actual.contarCartas());

            String entrada;
            int numFicha = -1;
            int pasos = -1;

            while (true) {
                System.out.print("Seleccione ficha a mover (1-3) o escriba SALIR: ");
                entrada = scanner.nextLine().trim();
                if (entrada.equalsIgnoreCase("SALIR")) {
                    jugando = false;
                    break;
                }
                try {
                    numFicha = Integer.parseInt(entrada) - 1;
                    if (numFicha < 0 || numFicha > 2) {
                        System.out.println("Número de ficha inválido. Debe ser entre 1 y 3.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor ingrese un número entre 1 y 3 o SALIR.");
                }
            }
            if (!jugando) break;

            while (true) {
                System.out.print("¿Cuántos pasos desea avanzar esa ficha?: ");
                entrada = scanner.nextLine().trim();
                try {
                    pasos = Integer.parseInt(entrada);
                    if (pasos < 1) {
                        System.out.println("El número de pasos debe ser mayor que cero.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Por favor ingrese un número entero positivo.");
                }
            }

            actual.avanzarFicha(numFicha, pasos, circulo.getTamano());
            Accion casillaActual = circulo.getCasilla(actual.getFichasAccion()[numFicha]);
            System.out.println("Acción: " + casillaActual.getTipo());

            switch (casillaActual.getTipo()) {
                case "Movimiento":
                    System.out.print("Origen (ej: A3): ");
                    String origen = scanner.nextLine().trim();
                    System.out.print("Destino (ej: B3): ");
                    String destino = scanner.nextLine().trim();
                    tablero.moverPieza(origen, destino);
                    break;
                case "TomarCarta":
                    if (casillaActual.getNombre().contains("Movimiento")) {
                        Carta cartaMov = mazoMov.tomarCarta();
                        if (cartaMov != null) {
                            actual.agregarCarta(cartaMov);
                            System.out.println("Tomó carta de movimiento: " + cartaMov.getDescripcion());
                        }
                    } else {
                        Carta cartaAtk = mazoAtk.tomarCarta();
                        if (cartaAtk != null) {
                            actual.agregarCarta(cartaAtk);
                            System.out.println("Tomó carta de ataque: " + cartaAtk.getDescripcion());
                        }
                    }
                    System.out.println("Cartas en mano: " + actual.contarCartas());
                    System.out.println("Cartas restantes en mazos: Movimiento(" +
                            mazoMov.cartasRestantes() + ") / Ataque(" + mazoAtk.cartasRestantes() + ")");
                    break;
                case "Ataque":
                case "Rotar":
                    System.out.println(actual.getNombre() + " realizó acción: " + casillaActual.getTipo());
                    break;
                case "Transicion":
                    System.out.println("Espacio de transición.");
                    break;
            }

            tablero.imprimirTablero();
            turno++;
        }

        System.out.println("Fin de la partida. Volviendo al menú.");
    }
}



import Model.*;

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
        Tablero tablero;
        CirculoAcciones circulo;
        MazoCartas mazoMov;
        MazoCartas mazoAtk;
        Jugador jugador1;
        Jugador jugador2;

        tablero = new Tablero();
        circulo = new CirculoAcciones();
        mazoMov = new MazoCartas("Movimiento");
        mazoAtk = new MazoCartas("Ataque");
        jugador1 = new Jugador("Jugador 1", "rojo");
        jugador2 = new Jugador("Jugador 2", "azul");

        boolean jugando = true;
        int turno = 1;

        while (jugando) {
            Jugador actual = (turno % 2 == 1) ? jugador1 : jugador2;
            System.out.println("\nTurno de " + actual.getNombre() + " (" + actual.getColor() + ")");
            circulo.imprimirCirculoConFichas(actual.getFichasAccion());
            System.out.println("Cartas en mano: " + actual.contarCartas());

            if (actual.contarCartas() > 0) {
                System.out.print("¿Desea usar una carta antes de mover? (s/n): ");
                String usarCarta = scanner.nextLine().trim().toLowerCase();
                if (usarCarta.equals("s")) {
                    usarCarta(actual, tablero);
                }
            }

            int numFicha = elegirFicha(actual);
            if (numFicha == -1) {
                jugando = false;
                break;
            }

            int pasos = elegirPasos();
            actual.avanzarFicha(numFicha, pasos, circulo.getTamano());
            Accion casillaActual = circulo.getCasilla(actual.getFichasAccion()[numFicha]);
            System.out.println("Acción: " + casillaActual.getTipo());

            realizarAccion(casillaActual.getTipo(), actual, numFicha, tablero, mazoMov, mazoAtk);
            tablero.imprimirTablero();
            turno++;
            if (!tablero.hayReinaViva(jugador1.getColor())) {
                System.out.println("¡" + jugador2.getNombre() + " ha ganado! La reina roja ha sido derrotada.");
                jugando = false;
            } else if (!tablero.hayReinaViva(jugador2.getColor())) {
                System.out.println("¡" + jugador1.getNombre() + " ha ganado! La reina azul ha sido derrotada.");
                jugando = false;
            }
        }

        System.out.println("Fin de la partida. Volviendo al menú.");
    }

    private int elegirFicha(Jugador jugador) {
        while (true) {
            System.out.print("Seleccione ficha a mover (1-3) o escriba SALIR: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase("SALIR")) return -1;
            try {
                int ficha = Integer.parseInt(entrada) - 1;
                if (ficha >= 0 && ficha <= 2) return ficha;
            } catch (NumberFormatException e) {

            }
            System.out.println("Entrada inválida. Intente de nuevo.");
        }
    }

    private int elegirPasos() {
        while (true) {
            System.out.print("¿Cuántos pasos desea avanzar esa ficha?: ");
            String entrada = scanner.nextLine().trim();
            try {
                int pasos = Integer.parseInt(entrada);
                if (pasos > 0) return pasos;
            } catch (NumberFormatException e) {
            }
            System.out.println("Entrada inválida. Ingrese un número positivo.");
        }
    }

    private void realizarAccion(String tipo, Jugador jugador, int numFicha, Tablero tablero, MazoCartas mazoMov, MazoCartas mazoAtk) {

        switch (tipo) {
            case "Movimiento":
                manejarMovimiento(jugador, tablero);
                break;

            case "Ataque":
                manejarAtaque(jugador, tablero);
                break;

            case "Rotar":
                System.out.println("\nROTANDO FICHAS...");
                System.out.println("1. Derecha");
                System.out.println("2. Izquierda");
                int opcion = scanner.nextInt();
                jugador.rotarFichas(opcion == 1);
                break;

            case "TomarCarta":
                manejarTomaCarta(jugador, mazoMov, mazoAtk, numFicha);
                break;
        }
    }

    private void manejarTomaCarta(Jugador jugador, MazoCartas mazoMov,
                                  MazoCartas mazoAtk, int numFicha) {
        boolean esMovimiento = (jugador.getFichasAccion()[numFicha] % 2 == 0);
        MazoCartas mazo = esMovimiento ? mazoMov : mazoAtk;
        String tipo = esMovimiento ? "Movimiento" : "Ataque";

        Carta carta = mazo.tomarCarta();
        if (carta != null) {
            jugador.agregarCarta(carta);
            System.out.printf("\nTOMASTE CARTA DE %s: %s\n",
                    tipo, carta.getDescripcion());
        } else {
            System.out.println("\n¡No hay cartas disponibles en el mazo de " + tipo + "!");
        }

        jugador.mostrarEstadoCartas();
    }
}

    private void usarCarta(Jugador jugador, Tablero tablero) {
        jugador.mostrarCartas();

        System.out.print("Seleccione el número de carta que desea usar (o escriba CANCELAR): ");
        String entrada = scanner.nextLine().trim();

        if (entrada.equalsIgnoreCase("CANCELAR")) return;

        try {
            int indice = Integer.parseInt(entrada) - 1;
            Carta carta = jugador.getCarta(indice);
            if (carta == null) {
                System.out.println("Índice inválido.");
                return;
            }

            String tipo = carta.getTipo();

            switch (tipo) {
                case "Movimiento":
                    System.out.print("Origen (ej: A3): ");
                    String origenMov = scanner.nextLine().trim();
                    System.out.print("Destino (máx. 2 casillas, ej: C3): ");
                    String destinoMov = scanner.nextLine().trim();
                    boolean movValido = tablero.moverPiezaDoble(origenMov, destinoMov, jugador.getColor());
                    if (movValido) jugador.removerCarta(indice);
                    break;
                case "Ataque":
                    if (exito) {
                        Pieza pieza = tablero.obtenerPieza(objetivo);
                        if (pieza instanceof Reina) {
                            System.out.println("¡REINA ELIMINADA! Fin del juego.");
                            return; // Termina el turno inmediatamente
                        }
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }

        private void manejarTransicion(Jugador jugador, int numFicha, CirculoAcciones circulo) {
            int posicion = jugador.getFichasAccion()[numFicha];

            if (posicion == 13) {
                System.out.println("La ficha regresa al inicio del círculo");
                jugador.getFichasAccion()[numFicha] = 0;
            } else if (posicion == 12) {
                System.out.println("Puedes avanzar al espacio final");
            }
        }
    }
}



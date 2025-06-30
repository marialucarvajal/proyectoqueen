import modelo.Tablero;
import modelo.Jugador;
import modelo.CirculoAcciones;
import modelo.cartas.MazoCartas;
import java.util.Scanner;

public class Juego {
    private boolean enEjecucion;
    private Scanner scanner;
    private Tablero tablero;
    private Jugador jugador1, jugador2;
    private CirculoAcciones circulo;
    private MazoCartas mazoMov, mazoAtk;

    public void iniciar() {
        scanner = new Scanner(System.in);
        enEjecucion = true;

        while (enEjecucion) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    iniciarPartida();
                    break;
                case "2":
                    enEjecucion = false;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\nQUEEN ON THE FIELD");
        System.out.println("1. Jugar partida");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void iniciarPartida() {

        tablero = new Tablero();
        circulo = new CirculoAcciones();
        mazoMov = new MazoCartas("Movimiento");
        mazoAtk = new MazoCartas("Ataque");
        jugador1 = new Jugador("Jugador 1", "rojo");
        jugador2 = new Jugador("Jugador 2", "azul");

        int turno = 0;
        boolean juegoActivo = true;

        while (juegoActivo) {
            Jugador actual = (turno % 2 == 0) ? jugador1 : jugador2;
            System.out.println("\n--- Turno de " + actual.getNombre() + " ---");

            tablero.mostrar();
            mostrarEstadoJugador(actual);

            if (!manejarTurno(actual)) {
                juegoActivo = false;
                continue;
            }

            if (tablero.reinaEliminada(actual.getColorContrario())) {
                System.out.println("¡" + actual.getNombre() + " ha ganado!");
                juegoActivo = false;
            }

            turno++;
        }
    }

    private void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\nAcciones disponibles:");
        circulo.mostrarSiguientesAcciones(jugador.getFichaActiva());

        System.out.println("\nCartas: " + jugador.getCartas().size() +
                " | Mazo Mov: " + mazoMov.cartasRestantes() +
                " | Mazo Atq: " + mazoAtk.cartasRestantes());
    }

    private boolean manejarTurno(Jugador jugador) {
        System.out.print("\nSeleccione ficha (1-3) o SALIR: ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("SALIR")) return false;

        try {
            int ficha = Integer.parseInt(input) - 1;
            if (ficha < 0 || ficha > 2) {
                System.out.println("Número inválido");
                return true;
            }

            System.out.print("Pasos a avanzar (1-3): ");
            int pasos = Integer.parseInt(scanner.nextLine());

            String accion = circulo.avanzarFicha(jugador, ficha, pasos);
            System.out.println("\nAcción: " + accion);
            ejecutarAccion(accion, jugador);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }

    private void ejecutarAccion(String tipo, Jugador jugador) {
        switch (tipo) {
            case "Movimiento":
                manejarMovimiento(jugador);
                break;
            case "Ataque":
                manejarAtaque(jugador);
                break;
            case "TomarCarta":
                MazoCartas mazo = (jugador.getFichaActiva() % 2 == 0) ? mazoMov : mazoAtk;
                jugador.tomarCarta(mazo);
                break;
            case "Rotar":
                jugador.rotarFichas();
                break;
        }
    }

    private void manejarMovimiento(Jugador jugador) {
        System.out.print("Ingrese movimiento (ej. A2 B2): ");
        String[] coords = scanner.nextLine().split(" ");
        if (coords.length == 2) {
            tablero.moverPieza(coords[0], coords[1], jugador.getColor());
        } else {
            System.out.println("Formato incorrecto");
        }
    }

    private void manejarAtaque(Jugador jugador) {
        if (jugador.getCartasAtaque().isEmpty()) {
            System.out.println("No tienes cartas de ataque");
            return;
        }

        System.out.print("Ingrese coordenada a atacar (ej. B2): ");
        String objetivo = scanner.nextLine().trim();
        if (tablero.atacar(objetivo, jugador.getColor())) {
            jugador.usarCartaAtaque();
        }
    }
}



import java.util.Scanner;

public class Juego {
    private boolean enEjecucion;
    private Scanner scanner;
    private Tablero tablero;
    private Jugador jugador1, jugador2;
    private CirculoAcciones circulo;
    private MazoCartas mazoMov, mazoAtk;

    public static void main(String[] args) {
        new Juego().iniciar();
    }

    public void iniciar() {
        scanner = new Scanner(System.in);
        enEjecucion = true;

        while (enEjecucion) {
            System.out.println("\nQUEEN ON THE FIELD");
            System.out.println("1. Jugar");
            System.out.println("2. Salir");
            System.out.print("Opción: ");

            switch (scanner.nextLine()) {
                case "1": iniciarPartida(); break;
                case "2": enEjecucion = false; break;
                default: System.out.println("Opción inválida");
            }
        }
        scanner.close();
    }

    private void iniciarPartida() {
        tablero = new Tablero();
        circulo = new CirculoAcciones();
        mazoMov = new MazoCartas("Movimiento");
        mazoAtk = new MazoCartas("Ataque");
        jugador1 = new Jugador("Jugador 1", "rojo");
        jugador2 = new Jugador("Jugador 2", "azul");

        int turno = 0;
        boolean partidaActiva = true;

        while (partidaActiva) {
            Jugador actual = (turno % 2 == 0) ? jugador1 : jugador2;
            System.out.println("\n--- Turno de " + actual.getNombre() + " ---");

            tablero.mostrar();
            mostrarEstado(actual);

            if (!manejarTurno(actual)) {
                partidaActiva = false;
                continue;
            }

            if (tablero.reinaEliminada(actual.getColorContrario())) {
                System.out.println("¡" + actual.getNombre() + " ha ganado!");
                partidaActiva = false;
            }

            turno++;
        }
    }

    private void mostrarEstado(Jugador jugador) {
        System.out.println("Fichas en círculo:");
        circulo.mostrarPosiciones(jugador.getFichas());

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

            System.out.print("Pasos a avanzar: ");
            int pasos = Integer.parseInt(scanner.nextLine());

            String accion = circulo.avanzarFicha(jugador, ficha, pasos);
            System.out.println("Acción: " + accion);

            ejecutarAccion(accion, jugador);

        } catch (Exception e) {
            System.out.println("Entrada inválida");
        }
        return true;
    }

    private void ejecutarAccion(String tipo, Jugador jugador) {
        switch (tipo) {
            case "Movimiento":
                System.out.print("Ingrese movimiento (ej. A2 B2): ");
                String[] coords = scanner.nextLine().split(" ");
                tablero.moverPieza(coords[0], coords[1], jugador.getColor());
                break;

            case "Ataque":
                System.out.print("Coordenada a atacar (ej. B2): ");
                tablero.atacar(scanner.nextLine(), jugador.getColor());
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
}



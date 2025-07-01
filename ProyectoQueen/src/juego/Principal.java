package juego;

import ui.Consola;
import ui.Imprimir;
import utils.InputAyuda;
import java.util.List;
import java.util.ArrayList;

public class Principal {
    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugadorActual;
    private CirculoAccion circulo;
    private Mazo mazoMovimiento;
    private Mazo mazoAtaque;
    private Mazo descarteMovimiento;
    private Mazo descarteAtaque;
    private List<String> accionesDisponibles = new ArrayList<>();

    public Principal() {
        inicializarJuego();
    }

    public void iniciar() {
        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        while(true) {
            Imprimir.mensaje("\n=== QUEEN ON THE FIELD ===");
            Imprimir.mensaje("1. Jugar partida");
            Imprimir.mensaje("2. Salir");

            int opcion = InputAyuda.obtenerEntero("Seleccione opción: ", 1, 2);

            if(opcion == 1) {
                inicializarJuego();
                jugarPartida();
            } else {
                System.exit(0);
            }
        }
    }

    private void jugarPartida() {
        boolean enPartida = true;

        while(enPartida) {
            Consola.mostrarTablero(tablero);
            Imprimir.mensaje("\nTurno del Jugador " + jugadorActual.getColor());

            mostrarEstadoCartas();

            Consola.mostrarAccionesDisponibles(accionesDisponibles);

            String input = InputAyuda.obtenerInput("Ingrese acción (o SALIR para terminar): ");

            if(input.equalsIgnoreCase("SALIR")) {
                enPartida = false;
                continue;
            }

            procesarAccion(input);
            cambiarTurno();
        }
    }

    private void inicializarJuego() {
        this.tablero = new Tablero();
        this.jugador1 = new Jugador("Rojo");
        this.jugador2 = new Jugador("Azul");
        this.jugadorActual = jugador1;
        this.circulo = new CirculoAccion();
        this.mazoMovimiento = new Mazo("MOVIMIENTO");
        this.mazoAtaque = new Mazo("ATAQUE");
        this.descarteMovimiento = new Mazo("DESCARTE_MOV");
        this.descarteAtaque = new Mazo("DESCARTE_ATAQ");
        this.tablero.colocarPiezasIniciales(jugador1, jugador2);
        this.accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
    }

    private void mostrarEstadoCartas() {
        Imprimir.mensaje("Cartas Movimiento: " + jugadorActual.getCartasCount() +
                " (Mazo: " + mazoMovimiento.cartasRestantes() + ")");
        Imprimir.mensaje("Cartas Ataque: " + jugadorActual.getCartasAtaqueCount() +
                " (Mazo: " + mazoAtaque.cartasRestantes() + ")");
    }

    private void procesarAccion(String accion) {
        switch(accion.toUpperCase()) {
            case "MOVER":
                manejarMovimiento();
                break;
            case "ATACAR":
                manejarAtaque();
                break;
            case "TOMAR_CARTA":
                tomarCarta();
                break;
            case "ROTAR":
                manejarRotacion();
                break;
            default:
                Imprimir.error("Acción no válida");
                return;
        }
        circulo.moverFichaPorAccion(jugadorActual, accion);
    }

    private void manejarMovimiento() {
        if(jugadorActual.getCartasMovimiento().isEmpty()) {
            Imprimir.error("No tienes cartas de movimiento");
            return;
        }

        List<Carta> cartasMov = jugadorActual.getCartasMovimiento();
        Imprimir.mensaje("Cartas de movimiento disponibles:");
        for(int i = 0; i < cartasMov.size(); i++) {
            Imprimir.mensaje((i+1) + ". " + cartasMov.get(i));
        }

        int seleccion = InputAyuda.obtenerEntero("Seleccione carta (1-" + cartasMov.size() + "): ", 1, cartasMov.size());
        Carta cartaSeleccionada = cartasMov.get(seleccion - 1);

        int[] origen = InputAyuda.obtenerCoordenadas("Ingrese coordenada de origen (ej. A1): ");
        int[] destino = InputAyuda.obtenerCoordenadas("Ingrese coordenada de destino: ");

        if(tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            Imprimir.mensaje("Movimiento exitoso con: " + cartaSeleccionada);
            jugadorActual.usarCartaMovimiento(cartaSeleccionada);
            descarteMovimiento.agregarCarta(cartaSeleccionada);
        } else {
            Imprimir.error("Movimiento inválido");
        }
    }

    private void tomarCarta() {
        Carta carta;
        if(jugadorActual == jugador1) {
            carta = mazoMovimiento.tomarCarta();
            if(carta == null) {
                reiniciarMazoMovimiento();
                carta = mazoMovimiento.tomarCarta();
            }
            if(carta != null) {
                jugadorActual.agregarCartaMovimiento(carta);
            }
        } else {
            carta = mazoAtaque.tomarCarta();
            if(carta == null) {
                reiniciarMazoAtaque();
                carta = mazoAtaque.tomarCarta();
            }
            if(carta != null) {
                jugadorActual.agregarCartaAtaque(carta);
            }
        }

        if(carta != null) {
            Imprimir.mensaje("Carta obtenida: " + carta);
        } else {
            Imprimir.error("No hay cartas disponibles");
        }
    }

    private void reiniciarMazoMovimiento() {
        while(descarteMovimiento.cartasRestantes() > 0) {
            mazoMovimiento.agregarCarta(descarteMovimiento.tomarCarta());
        }
        mazoMovimiento.barajar();
    }

    private void reiniciarMazoAtaque() {
        while(descarteAtaque.cartasRestantes() > 0) {
            mazoAtaque.agregarCarta(descarteAtaque.tomarCarta());
        }
        mazoAtaque.barajar();
    }

    private void manejarAtaque() {
        Imprimir.mensaje("[Sistema] ¡Atacó!");
    }

    private void manejarRotacion() {
        Imprimir.mensaje("[Sistema] ¡Rotación completada!");
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
        accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
        Imprimir.mensaje("\n=== Turno cambiado a Jugador " + jugadorActual.getColor() + " ===");
    }
}

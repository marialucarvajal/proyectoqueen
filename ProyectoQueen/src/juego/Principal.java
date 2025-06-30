package juego;

import ui.Consola;
import ui.Imprimir;
import utils.InputAyuda;

import java.util.List;

public class Principal {
    private Tablero tablero;
    private Jugador jugadorActual;
    private CirculoAccion circulo;
    private Mazo mazoMovimiento;
    private Mazo mazoAtaque;

    public void iniciar() {
        inicializarJuego();
        bucleJuego();
    }

    private void inicializarJuego() {
        mazoMovimiento = new Mazo("MOVIMIENTO");
        mazoAtaque = new Mazo("ATAQUE");
        tablero = new Tablero();
        circulo = new CirculoAccion();

        Jugador jugador1 = new Jugador("Rojo");
        Jugador jugador2 = new Jugador("Azul");

        tablero.colocarPiezasIniciales(jugador1, jugador2);
        jugadorActual = jugador1;
    }

    private void bucleJuego() {
        boolean enJuego = true;

        while (enJuego) {
            Consola.mostrarTablero(tablero);
            Consola.mostrarEstadoJugador(jugadorActual);

            List<String> acciones = circulo.obtenerAccionesDisponibles(jugadorActual);
            Consola.mostrarAccionesDisponibles(acciones);

            String input = InputAyuda.obtenerInput("Seleccione acción: ");

            if (input.equalsIgnoreCase("SALIR")) {
                enJuego = false;
            } else {
                procesarAccion(input);
            }
        }
    }

    private void procesarAccion(String accion) {
        switch(accion.toUpperCase()) {
            case "MOVER":
                manejarMovimiento();
                break;
            case "ATACAR":
                Imprimir.mensaje("[Sistema] ¡Atacó!");
                break;
            case "TOMAR_CARTA":
                tomarCarta();
                break;
            case "ROTAR":
                Imprimir.mensaje("[Sistema] ¡Rotación completada!");
                break;
            default:
                Imprimir.error("Acción no válida");
        }
    }

    private void manejarMovimiento() {
        int[] origen = InputAyuda.obtenerCoordenadas("Origen (ej. A1): ");
        int[] destino = InputAyuda.obtenerCoordenadas("Destino: ");

        if (tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            Imprimir.mensaje("Movimiento exitoso");
        } else {
            Imprimir.error("Movimiento inválido");
        }
    }

    private void tomarCarta() {
        Carta carta = jugadorActual.getColor().equals("Rojo")
                ? mazoMovimiento.tomarCarta()
                : mazoAtaque.tomarCarta();

        jugadorActual.agregarCarta(carta);
        Imprimir.mensaje("Carta obtenida: " + carta);
    }
}

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
    private List<String> accionesDisponibles;

    public Principal() {
        inicializarJuego();
    }

    public void iniciar() {
        boolean juegoEnCurso = true;

        Imprimir.mensaje("=== QUEEN ON THE FIELD ===");
        Imprimir.mensaje("Turno del Jugador " + jugadorActual.getColor());

        while (juegoEnCurso) {
            Imprimir.mostrarAccionesDisponibles(accionesDisponibles);
            String input = InputAyuda.obtenerInput("Seleccione acción: ");

            if (input.equalsIgnoreCase("SALIR")) {
                juegoEnCurso = false;
                Imprimir.mensaje("Juego terminado.");
                continue;
            }

            procesarAccion(input);
        }
    }

    public void inicializarJuego() {
        this.tablero = new Tablero();
        this.jugador1 = new Jugador("Rojo");
        this.jugador2 = new Jugador("Azul");
        this.jugadorActual = this.jugador1;
        this.circulo = new CirculoAccion();
        this.mazoMovimiento = new Mazo("MOVIMIENTO");
        this.mazoAtaque = new Mazo("ATAQUE");
        this.tablero.colocarPiezasIniciales(jugador1, jugador2);
        this.accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
    }

    public void procesarAccion(String accion) {
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
                Imprimir.mensaje("[Sistema] ¡Rotación completada!");
                circulo.moverFicha(jugadorActual, 2, 2);
                break;
            default:
                Imprimir.error("Acción no válida");
                return;
        }
        cambiarTurno();
    }

    private void manejarMovimiento() {
        if (!jugadorActual.tieneCartasMovimiento()) {
            Imprimir.error("No tienes cartas de movimiento");
            return;
        }

        List<Carta> cartasMov = jugadorActual.getCartasMovimiento();
        Imprimir.mensaje("Cartas de movimiento disponibles:");
        for (int i = 0; i < cartasMov.size(); i++) {
            Imprimir.mensaje((i + 1) + ". " + cartasMov.get(i));
        }

        int seleccion = InputAyuda.obtenerEntero("Seleccione carta (1-" + cartasMov.size() + "): ", 1, cartasMov.size());
        Carta cartaSeleccionada = cartasMov.get(seleccion - 1);

        int[] origen = InputAyuda.obtenerCoordenadas("Ingrese coordenada de origen (ej. A1): ");
        if (origen == null || !tablero.piezaPerteneceAJugador(origen[0], origen[1], jugadorActual)) {
            Imprimir.error("Coordenadas inválidas o pieza no te pertenece");
            return;
        }

        int[] destino = InputAyuda.obtenerCoordenadas("Ingrese coordenada de destino: ");
        if (destino == null) {
            Imprimir.error("Coordenadas inválidas");
            return;
        }

        boolean movimientoValido = false;
        switch (cartaSeleccionada.getEfecto()) {
            case "Mover pieza 1 casilla en cualquier dirección":
                movimientoValido = tablero.moverPieza(origen[0], origen[1], destino[0], destino[1]);
                break;
            case "Mover pieza 2 casillas en línea recta":
                movimientoValido = tablero.moverPiezaDistancia(origen[0], origen[1], destino[0], destino[1], 2);
                break;
            case "Mover pieza en L como caballo de ajedrez":
                movimientoValido = tablero.moverPiezaEnL(origen[0], origen[1], destino[0], destino[1]);
                break;
        }

        if (movimientoValido) {
            jugadorActual.getCartas().remove(cartaSeleccionada);
            Imprimir.mensaje("Movimiento exitoso usando: " + cartaSeleccionada);
            circulo.moverFicha(jugadorActual, 0, 3);
        } else {
            Imprimir.error("Movimiento inválido para esta carta");
        }
    }

    private void manejarAtaque() {
        // Implementación similar a manejarMovimiento pero para ataques
        Imprimir.mensaje("[Sistema] ¡Atacó!");
        circulo.moverFicha(jugadorActual, 0, 3);
    }

    private void tomarCarta() {
        Carta carta = jugadorActual.getColor().equals("Rojo") ?
                mazoMovimiento.tomarCarta() : mazoAtaque.tomarCarta();

        if (carta != null) {
            jugadorActual.agregarCarta(carta);
            Imprimir.mensaje("Carta obtenida: " + carta);
            circulo.moverFicha(jugadorActual, 1, 4);
        } else {
            Imprimir.error("No hay cartas disponibles en el mazo");
        }
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
        accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
        Imprimir.mensaje("\n=== Turno cambiado a Jugador " + jugadorActual.getColor() + " ===");
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }
}

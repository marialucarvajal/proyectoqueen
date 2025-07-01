
package juego;

import ui.Consola;
import ui.Imprimir;
import utils.InputAyuda;
import utils.ConvertirCoordenadas;
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
    private boolean enPartida;

    public Principal() {
        inicializarJuego();
    }

    public void iniciar() {
        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        while(true) {
            Imprimir.mensaje("\nQUEEN ON THE FIELD");
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
        enPartida = true;

        while(enPartida) {
            Consola.mostrarTablero(tablero);
            Imprimir.mensaje("\nTurno del Jugador " + jugadorActual.getColor());
            mostrarEstadoCartas();

            accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
            Consola.mostrarAccionesDisponibles(accionesDisponibles);

            String input = InputAyuda.obtenerInput("Ingrese acción (o SALIR para terminar): ").toUpperCase();

            if(input.equals("SALIR")) {
                enPartida = false;
                continue;
            }

            procesarAccion(input);
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
        Imprimir.mensaje("Cartas Movimiento: " + jugadorActual.getCartasMovimiento().size() +
                " (Mazo: " + mazoMovimiento.cartasRestantes() + ")");
        Imprimir.mensaje("Cartas Ataque: " + jugadorActual.getCartasAtaque().size() +
                " (Mazo: " + mazoAtaque.cartasRestantes() + ")");
    }

    private boolean procesarAccion(String accion) {
        try {
            int seleccion = Integer.parseInt(accion);
            if(seleccion == 0) return false;
            if(seleccion > 0 && seleccion <= accionesDisponibles.size()) {
                accion = accionesDisponibles.get(seleccion-1);
            }
        } catch(NumberFormatException e) {

        }

        if(!accionesDisponibles.contains(accion)) {
            Imprimir.error("Acción no disponible");
            return false;
        }

        boolean accionExitosa = false;
        switch(accion) {
            case "TOMAR_CARTA":
                accionExitosa = tomarCarta();
                break;
            case "MOVIMIENTO":
                accionExitosa = manejarMovimiento();
                break;
            case "ATAQUE":
                accionExitosa = manejarAtaque();
                break;
            case "ROTACION":
                accionExitosa = manejarRotacion();
                break;
            default:
                Imprimir.error("Acción no reconocida");
                return false;
        }

        if(accionExitosa) {
            circulo.usarAccion(jugadorActual, accion);
            cambiarTurno();
        }
        return accionExitosa;
    }

    private boolean manejarMovimiento() {
        if(jugadorActual.getCartasMovimiento().isEmpty()) {
            Imprimir.mensaje("No tiene cartas de movimiento");
            return false;
        }

        List<Carta> cartasMov = jugadorActual.getCartasMovimiento();
        Imprimir.mensaje("\nCartas de movimiento disponibles:");
        for(int i = 0; i < cartasMov.size(); i++) {
            Imprimir.mensaje((i+1) + ". " + cartasMov.get(i).toString());
        }

        int seleccion = InputAyuda.obtenerEntero("Seleccione carta (1-" + cartasMov.size() + "): ", 1, cartasMov.size());
        Carta cartaSeleccionada = cartasMov.get(seleccion - 1);

        int[] origen = InputAyuda.obtenerCoordenadas("Origen (ej. A1): ");
        if(origen == null) return false;

        System.out.println("DEBUG: Coordenadas origen interpretadas - fila: " + origen[0] + ", columna: " + origen[1]);

        Pieza piezaOrigen = tablero.obtenerPieza(origen[0], origen[1]);
        if(piezaOrigen == null) {
            Imprimir.error("No hay ninguna pieza en " + ConvertirCoordenadas.aNotacionTablero(origen[0], origen[1]));
            return false;
        }
        if(!piezaOrigen.getJugador().equals(jugadorActual)) {
            Imprimir.error("La pieza en " + ConvertirCoordenadas.aNotacionTablero(origen[0], origen[1]) +
                    " no te pertenece");
            return false;
        }

        int[] destino = InputAyuda.obtenerCoordenadas("Destino: ");
        if(destino == null) return false;

        System.out.println("DEBUG: Coordenadas destino interpretadas - fila: " + destino[0] + ", columna: " + destino[1]);

        if(piezaOrigen.esReina()) {
            if(!validarMovimientoReina(origen, destino)) return false;
        } else {
            if(!validarMovimientoPeon(origen, destino)) return false;
        }

        if(tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            jugadorActual.usarCartaMovimiento(cartaSeleccionada);
            descarteMovimiento.agregarCarta(cartaSeleccionada);
            Imprimir.mensaje("\n¡Movimiento exitoso!");
            return true;
        }

        Imprimir.error("Movimiento no válido");
        return false;
    }

    private boolean validarMovimientoPeon(int[] origen, int[] destino) {
        int dir = jugadorActual.getColor().equals("Rojo") ? 1 : -1;
        int diffFila = destino[0] - origen[0];
        int diffCol = Math.abs(destino[1] - origen[1]);

        if(diffCol == 0 && diffFila == dir) {
            if(tablero.obtenerPieza(destino[0], destino[1]) != null) {
                Imprimir.error("No puede mover a una casilla ocupada");
                return false;
            }
            return true;
        }

        if(diffCol == 1 && diffFila == dir) {
            Pieza piezaDestino = tablero.obtenerPieza(destino[0], destino[1]);
            if(piezaDestino == null) {
                Imprimir.error("No hay pieza enemiga para atacar");
                return false;
            }
            if(piezaDestino.getJugador().equals(jugadorActual)) {
                Imprimir.error("No puede atacar su propia pieza");
                return false;
            }
            return true;
        }

        Imprimir.error("Movimiento no válido para peón");
        return false;
    }

    private boolean validarMovimientoReina(int[] origen, int[] destino) {
        int diffFila = Math.abs(destino[0] - origen[0]);
        int diffCol = Math.abs(destino[1] - origen[1]);
        Pieza piezaDestino = tablero.obtenerPieza(destino[0], destino[1]);

        if((diffFila == 0 && diffCol > 0) || (diffCol == 0 && diffFila > 0)) {
            if(piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                Imprimir.error("No puede mover a una casilla ocupada por su propia pieza");
                return false;
            }
            return true;
        }

        if(diffFila == diffCol && diffFila > 0) {
            if(piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                Imprimir.error("No puede mover a una casilla ocupada por su propia pieza");
                return false;
            }
            return true;
        }

        Imprimir.error("Movimiento no válido para reina");
        return false;
    }

    private boolean tomarCarta() {
        Carta carta;
        if(jugadorActual.getColor().equals("Rojo")) {
            if(mazoMovimiento.estaVacio()) {
                if(descarteMovimiento.cartasRestantes() > 0) {
                    reiniciarMazoMovimiento();
                } else {
                    Imprimir.error("No hay más cartas de movimiento disponibles");
                    return false;
                }
            }
            carta = mazoMovimiento.tomarCarta();
            jugadorActual.agregarCartaMovimiento(carta);
        } else {
            if(mazoAtaque.estaVacio()) {
                if(descarteAtaque.cartasRestantes() > 0) {
                    reiniciarMazoAtaque();
                } else {
                    Imprimir.error("No hay más cartas de ataque disponibles");
                    return false;
                }
            }
            carta = mazoAtaque.tomarCarta();
            jugadorActual.agregarCartaAtaque(carta);
        }

        Imprimir.mensaje("Carta obtenida: " + carta);
        return true;
    }

    private void reiniciarMazoMovimiento() {
        while(descarteMovimiento.cartasRestantes() > 0) {
            mazoMovimiento.agregarCarta(descarteMovimiento.tomarCarta());
        }
        mazoMovimiento.barajar();
        Imprimir.mensaje("Mazo de movimiento reiniciado");
    }

    private void reiniciarMazoAtaque() {
        while(descarteAtaque.cartasRestantes() > 0) {
            mazoAtaque.agregarCarta(descarteAtaque.tomarCarta());
        }
        mazoAtaque.barajar();
        Imprimir.mensaje("Mazo de ataque reiniciado");
    }

    private boolean manejarAtaque() {
        if(jugadorActual.getCartasAtaque().isEmpty()) {
            Imprimir.error("No tiene cartas de ataque");
            return false;
        }

        Carta carta = jugadorActual.getCartasAtaque().get(0);
        jugadorActual.usarCartaAtaque(carta);
        descarteAtaque.agregarCarta(carta);

        Imprimir.mensaje("Atacó!");
        mostrarEstadoCartas();
        return true;
    }

    private boolean manejarRotacion() {
        Imprimir.mensaje("Rotó!");
        mostrarEstadoCartas();
        return true;
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
        accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
        Imprimir.mensaje("\nTurno cambiado a Jugador " + jugadorActual.getColor());
    }
}

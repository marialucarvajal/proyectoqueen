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
    private boolean enPartida;

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
            // No es número, continuar
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
            Imprimir.mensaje("No tienes cartas de movimiento");
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

        int[] destino = InputAyuda.obtenerCoordenadas("Destino: ");
        if(destino == null) return false;

        if(!validarMovimientoSegunCarta(origen, destino, cartaSeleccionada)) {
            return false;
        }

        if(tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            Imprimir.mensaje("\n¡Movimiento exitoso con " + cartaSeleccionada.toString() + "!");
            jugadorActual.getCartasMovimiento().remove(cartaSeleccionada);
            descarteMovimiento.agregarCarta(cartaSeleccionada);
            return true;
        } else {
            Imprimir.mensaje("No se puede mover a esa posición");
            return false;
        }
    }

    private boolean validarMovimientoSegunCarta(int[] origen, int[] destino, Carta carta) {
        int diffFila = Math.abs(destino[0] - origen[0]);
        int diffCol = Math.abs(destino[1] - origen[1]);
        Pieza piezaDestino = tablero.obtenerPieza(destino[0], destino[1]);

        // Validar según el efecto de la carta
        switch(carta.getEfecto()) {
            case "Mover en L como caballo":
                if ((diffFila == 2 && diffCol == 1) || (diffFila == 1 && diffCol == 2)) {
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Movimiento en L debe ser 2 espacios en una dirección y 1 en perpendicular");
                return false;

            case "Mover 1 espacio diagonal":
                if (diffFila == 1 && diffCol == 1) {
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 1 espacio diagonal");
                return false;

            case "Mover 1 espacio cualquier dirección":
                if (diffFila <= 1 && diffCol <= 1 && (diffFila + diffCol) > 0) {
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 1 espacio en cualquier dirección");
                return false;

            default:
                Imprimir.error("Tipo de movimiento no reconocido: " + carta.getEfecto());
                return false;
        }
    }

    private boolean tomarCarta() {
        Carta carta;
        if(jugadorActual.getColor().equals("Rojo")) {
            // Jugador Rojo solo recibe cartas de MOVIMIENTO
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
            // Jugador Azul solo recibe cartas de ATAQUE
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
        Imprimir.mensaje("[Sistema] ¡Atacó!");
        return true;
    }

    private boolean manejarRotacion() {
        Imprimir.mensaje("[Sistema] ¡Rotó!");
        return true;
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
        accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
        Imprimir.mensaje("\n=== Turno cambiado a Jugador " + jugadorActual.getColor() + " ===");
    }
}

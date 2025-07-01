package juego;

import ui.Consola;
import ui.Imprimir;
import utils.ConvertirCoordenadas;
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

            String input = InputAyuda.obtenerInput("Ingrese acción (o SALIR para terminar): ");

            if(input.equalsIgnoreCase("SALIR")) {
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
        this.enPartida = true;
    }

    private void mostrarEstadoCartas() {
        Imprimir.mensaje("Cartas Movimiento: " + jugadorActual.getCartasMovimiento().size() +
                " (Mazo: " + mazoMovimiento.cartasRestantes() + ")");
        Imprimir.mensaje("Cartas Ataque: " + jugadorActual.getCartasAtaque().size() +
                " (Mazo: " + mazoAtaque.cartasRestantes() + ")");
    }

    private boolean procesarAccion(String accion) {
        accion = accion.toUpperCase().trim();

        if(!accionesDisponibles.contains(accion)) {
            try {
                int seleccion = Integer.parseInt(accion);
                if(seleccion > 0 && seleccion <= accionesDisponibles.size()) {
                    accion = accionesDisponibles.get(seleccion-1);
                } else {
                    Imprimir.error("Selección inválida.");
                    return false;
                }
            } catch(NumberFormatException e) {
                Imprimir.error("Acción no disponible en este turno.");
                return false;
            }
        }

        boolean accionExitosa = false;
        try {
            switch(accion.toUpperCase()) {
                case "TOMAR_CARTA":
                    if (jugadorActual.getColor().equals("Rojo") &&
                            mazoMovimiento.estaVacio() && descarteMovimiento.estaVacio()) {
                        Imprimir.error("No hay cartas de movimiento disponibles");
                        return false;
                    } else if (mazoAtaque.estaVacio() && descarteAtaque.estaVacio()) {
                        Imprimir.error("No hay cartas de ataque disponibles");
                        return false;
                    }
                    accionExitosa = tomarCarta();
                    break;

                case "MOVIMIENTO":
                    if (jugadorActual.getCartasMovimiento().isEmpty()) {
                        Imprimir.error("No tienes cartas de movimiento");
                        return false;
                    }
                    if (tablero.obtenerPiezasJugador(jugadorActual).isEmpty()) {
                        Imprimir.error("No tienes piezas para mover");
                        return false;
                    }
                    accionExitosa = manejarMovimiento();
                    break;

                case "ATACAR":
                    if (jugadorActual.getCartasAtaque().isEmpty()) {
                        Imprimir.error("No tienes cartas de ataque");
                        return false;
                    }
                    if (tablero.obtenerPiezasJugador(jugadorActual).isEmpty()) {
                        Imprimir.error("No tienes piezas para atacar");
                        return false;
                    }
                    accionExitosa = manejarAtaque();
                    break;

                case "ROTAR":
                    accionExitosa = manejarRotacion();
                    break;

                default:
                    Imprimir.error("Acción no reconocida: " + accion);
                    return false;
            }
        } catch (Exception e) {
            Imprimir.error("Error al procesar acción: " + e.getMessage());
            return false;
        }

        if (accionExitosa) {
            circulo.usarAccion(jugadorActual, accion); // Nuevo método para actualizar fichas
            cambiarTurno();
        }
        return accionExitosa;
    }

    private boolean manejarMovimiento() {
        if(jugadorActual.getCartasMovimiento().isEmpty()) {
            Imprimir.error("No tienes cartas de movimiento");
            return false;
        }

        List<Carta> cartasMov = jugadorActual.getCartasMovimiento();
        Imprimir.mensaje("\nCartas de movimiento disponibles:");
        for(int i = 0; i < cartasMov.size(); i++) {
            Imprimir.mensaje((i+1) + ". " + cartasMov.get(i));
        }

        int seleccion = InputAyuda.obtenerEntero("\nSeleccione carta (1-" + cartasMov.size() + "): ", 1, cartasMov.size());
        Carta cartaSeleccionada = cartasMov.get(seleccion - 1);

        int[] origen = InputAyuda.obtenerCoordenadas("Origen (ej. A1): ");
        if(origen == null) return false;

        int[] destino = InputAyuda.obtenerCoordenadas("Destino: ");
        if(destino == null) return false;

        if(!validarMovimientoSegunCarta(origen, destino, cartaSeleccionada)) {
            return false;
        }

        if(tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            Imprimir.mensaje("\n¡Movimiento exitoso con " + cartaSeleccionada + "!");
            jugadorActual.usarCartaMovimiento(cartaSeleccionada);
            descarteMovimiento.agregarCarta(cartaSeleccionada);
            return true;
        } else {
            Imprimir.error("No se puede mover a esa posición");
            return false;
        }
    }

    private boolean validarMovimientoSegunCarta(int[] origen, int[] destino, Carta carta) {
        int diffFila = Math.abs(destino[0] - origen[0]);
        int diffCol = Math.abs(destino[1] - origen[1]);
        Pieza piezaDestino = tablero.obtenerPieza(destino[0], destino[1]);

        switch(carta.getEfecto()) {
            case "Mover 3 espacios en línea recta":
                if ((diffFila == 3 && diffCol == 0) || (diffFila == 0 && diffCol == 3)) {
                    if (!caminoLibre(origen, destino)) {
                        return false;
                    }
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 3 espacios horizontal o vertical");
                return false;

            case "Mover 1 espacio horizontal/vertical":
                if ((diffFila == 1 && diffCol == 0) || (diffFila == 0 && diffCol == 1)) {
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 1 espacio horizontal o vertical");
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

            case "Mover 2 espacios horizontales":
                if (diffFila == 0 && diffCol == 2) {
                    if (!caminoLibre(origen, destino)) {
                        return false;
                    }
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 2 espacios horizontales");
                return false;

            case "Mover 2 espacios verticales":
                if (diffFila == 2 && diffCol == 0) {
                    int paso = Integer.compare(destino[0], origen[0]);

                    int filaIntermedia = origen[0] + paso;
                    if (tablero.obtenerPieza(filaIntermedia, origen[1]) != null) {
                        String pos = ConvertirCoordenadas.aNotacionTablero(filaIntermedia, origen[1]);
                        Imprimir.error("Camino bloqueado por pieza en " + pos);
                        return false;
                    }

                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 2 espacios verticales");
                return false;

            case "Mover 2 espacios en diagonal":
                if (diffFila == 2 && diffCol == 2) {
                    int filaIntermedia = (origen[0] + destino[0]) / 2;
                    int colIntermedia = (origen[1] + destino[1]) / 2;
                    if (tablero.obtenerPieza(filaIntermedia, colIntermedia) != null) {
                        String pos = ConvertirCoordenadas.aNotacionTablero(filaIntermedia, colIntermedia);
                        Imprimir.error("Camino bloqueado por pieza en " + pos);
                        return false;
                    }
                    if (piezaDestino != null && piezaDestino.getJugador().equals(jugadorActual)) {
                        Imprimir.error("No puedes mover a una casilla ocupada por tu propia pieza");
                        return false;
                    }
                    return true;
                }
                Imprimir.error("Debe mover exactamente 2 espacios en diagonal");
                return false;

            default:
                Imprimir.error("Tipo de movimiento no reconocido: " + carta.getEfecto());
                return false;
        }
    }

    private boolean caminoLibre(int[] origen, int[] destino) {
        int pasoFila = Integer.compare(destino[0], origen[0]);
        int pasoCol = Integer.compare(destino[1], origen[1]);

        for (int i = 1; i < Math.max(Math.abs(destino[0]-origen[0]), Math.abs(destino[1]-origen[1])); i++) {
            int filaIntermedia = origen[0] + (i * pasoFila);
            int colIntermedia = origen[1] + (i * pasoCol);

            if (tablero.obtenerPieza(filaIntermedia, colIntermedia) != null) {
                String pos = ConvertirCoordenadas.aNotacionTablero(filaIntermedia, colIntermedia);
                Imprimir.error("Camino bloqueado por pieza en " + pos);
                return false;
            }
        }
        return true;
    }

    private boolean tomarCarta() {
        Carta carta;
        if(jugadorActual.getColor().equals("Rojo")) {
            carta = mazoMovimiento.tomarCarta();
            if(carta == null) {
                reiniciarMazoMovimiento();
                carta = mazoMovimiento.tomarCarta();
            }
            if(carta != null) {
                jugadorActual.agregarCartaMovimiento(carta);
                Imprimir.mensaje("Carta obtenida: " + carta);
                return true;
            }
        } else {
            carta = mazoAtaque.tomarCarta();
            if(carta == null) {
                reiniciarMazoAtaque();
                carta = mazoAtaque.tomarCarta();
            }
            if(carta != null) {
                jugadorActual.agregarCartaAtaque(carta);
                Imprimir.mensaje("Carta obtenida: " + carta);
                return true;
            }
        }

        Imprimir.error("No hay cartas disponibles");
        return false;
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
            Imprimir.error("No tienes cartas de ataque");
            return false;
        }

        List<Carta> cartasAtq = jugadorActual.getCartasAtaque();
        Imprimir.mensaje("Cartas de ataque disponibles:");
        for(int i = 0; i < cartasAtq.size(); i++) {
            Imprimir.mensaje((i+1) + ". " + cartasAtq.get(i));
        }

        int seleccion = InputAyuda.obtenerEntero("Seleccione carta (1-" + cartasAtq.size() + "): ", 1, cartasAtq.size());
        Carta cartaSeleccionada = cartasAtq.get(seleccion - 1);

        int[] origen = InputAyuda.obtenerCoordenadas("Ingrese coordenada de origen (ej. A1): ");
        if(origen == null) return false;

        int[] destino = InputAyuda.obtenerCoordenadas("Ingrese coordenada de destino: ");
        if(destino == null) return false;

        Imprimir.mensaje("[Sistema] ¡Atacó con " + cartaSeleccionada + "!");
        jugadorActual.usarCartaAtaque(cartaSeleccionada);
        descarteAtaque.agregarCarta(cartaSeleccionada);
        return true;
    }

    private boolean manejarRotacion() {
        Imprimir.mensaje("[Sistema] ¡Rotación completada!");
        return true;
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

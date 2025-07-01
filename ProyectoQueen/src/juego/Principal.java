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
    private List<String> accionesDisponibles = new ArrayList<>();

    public Principal() {
        this.tablero = new Tablero();
        this.jugador1 = new Jugador("Rojo");
        this.jugador2 = new Jugador("Azul");
        this.jugadorActual = this.jugador1;
        this.circulo = new CirculoAccion();
        this.mazoMovimiento = new Mazo("MOVIMIENTO");
        this.mazoAtaque = new Mazo("ATAQUE");
        this.accionesDisponibles = new ArrayList<>();
    }

    public void iniciar() {
        inicializarJuego();
        boolean juegoEnCurso = true;

        Consola.mostrarTablero(tablero);
        Imprimir.mensaje("=== QUEEN ON THE FIELD ===");
        Imprimir.mensaje("Turno del Jugador " + jugadorActual.getColor());

        while (juegoEnCurso) {
            Consola.mostrarAccionesDisponibles(accionesDisponibles);
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
        this.jugadorActual = this.jugador1; // Asegura esta asignación
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
                Imprimir.mensaje("[Sistema] ¡Atacó!");
                circulo.moverFicha(jugadorActual, 0, 3);
                break;
            case "TOMAR_CARTA":
                tomarCarta();
                circulo.moverFicha(jugadorActual, 1, 4);
                break;
            case "ROTAR":
                Imprimir.mensaje("[Sistema] ¡Rotación completada!");
                circulo.moverFicha(jugadorActual, 2, 2);
                break;
            default:
                Imprimir.error("Acción no válida");
                return; // No cambiar turno si la acción fue inválida
        }
        cambiarTurno();
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugador1) ? jugador2 : jugador1;
        accionesDisponibles = circulo.obtenerAccionesDisponibles(jugadorActual);
        System.out.println("\n=== Turno cambiado a Jugador " + jugadorActual.getColor() + " ===");
    }

    private void manejarMovimiento() {
        int[] origen = InputAyuda.obtenerCoordenadas("Ingrese coordenada de origen (ej. A1): ");
        int[] destino = InputAyuda.obtenerCoordenadas("Ingrese coordenada de destino: ");

        if (origen == null || destino == null) {
            Imprimir.error("Coordenadas inválidas");
            return;
        }

        if (tablero.moverPieza(origen[0], origen[1], destino[0], destino[1])) {
            Imprimir.mensaje("Movimiento exitoso");
            circulo.moverFicha(jugadorActual, 0, 3);
        } else {
            Imprimir.error("Movimiento inválido");
        }
    }

    private void tomarCarta() {
        Carta carta = jugadorActual.getColor().equals("Rojo") ?
                mazoMovimiento.tomarCarta() : mazoAtaque.tomarCarta();

        if (carta != null) {
            jugadorActual.agregarCarta(carta);
            Imprimir.mensaje("Carta obtenida: " + carta);
        } else {
            Imprimir.error("No hay cartas disponibles en el mazo");
        }
    }

    public Jugador getJugadorActual() {
        return this.jugadorActual;
    }
}

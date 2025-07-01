package juego;

import ui.Consola;
import utils.ConvertirCoordenadas;

public class PruebaFinal {
    public static void main(String[] args) {
        probarMovimientos();
        probarCartas();
        probarTurnos();
    }

    private static void probarMovimientos() {
        System.out.println("\n=== PRUEBA DE MOVIMIENTOS ===");
        Tablero tablero = new Tablero();
        Jugador rojo = new Jugador("Rojo");
        Jugador azul = new Jugador("Azul");
        tablero.colocarPiezasIniciales(rojo, azul);

        // Mostrar estado inicial
        Consola.mostrarTablero(tablero);

        // Movimiento válido reina roja (A1->B2)
        System.out.println("\nMovimiento A1->B2:");
        boolean mov1 = tablero.moverPieza(0, 0, 1, 1);
        System.out.println("Resultado: " + (mov1 ? "ÉXITO" : "FALLÓ"));
        Consola.mostrarTablero(tablero);

        // Movimiento válido reina azul (F5->E5)
        System.out.println("\nMovimiento F5->E5:");
        boolean mov2 = tablero.moverPieza(5, 4, 4, 4);
        System.out.println("Resultado: " + (mov2 ? "ÉXITO" : "FALLÓ"));
        Consola.mostrarTablero(tablero);

        // Movimiento inválido (origen vacío)
        System.out.println("\nMovimiento inválido (C1->C2):");
        boolean mov3 = tablero.moverPieza(2, 0, 2, 1);
        System.out.println("Resultado: " + (!mov3 ? "CORRECTO (debió fallar)" : "ERROR"));
    }

    private static void probarCartas() {
        System.out.println("\n=== PRUEBA DE CARTAS ===");
        Mazo mazo = new Mazo("MOVIMIENTO");
        Jugador jugador = new Jugador("Rojo");

        // Tomar 3 cartas
        System.out.println("\nTomando 3 cartas:");
        for (int i = 0; i < 3; i++) {
            Carta carta = mazo.tomarCarta();
            jugador.agregarCarta(carta);
            System.out.println("Carta " + (i+1) + ": " + carta);
        }

        System.out.println("\nCartas del jugador: " + jugador.getCartas().size());
    }

    private static void probarTurnos() {
        System.out.println("\n=== PRUEBA DE TURNOS ===");
        Principal juego = new Principal();

        // Inicializa el juego correctamente
        juego.inicializarJuego();

        System.out.println("\nTurno inicial:");
        Jugador jugadorActual = juego.getJugadorActual();
        System.out.println("Jugador actual: " +
                (jugadorActual != null ? jugadorActual.getColor() : "NO INICIALIZADO"));

        // Simular cambio de turno
        juego.procesarAccion("ATACAR"); // Esto llamará internamente a cambiarTurno()

        System.out.println("\nDespués de cambiar turno:");
        jugadorActual = juego.getJugadorActual();
        System.out.println("Jugador actual: " +
                (jugadorActual != null ? jugadorActual.getColor() : "NO INICIALIZADO"));
    }
}

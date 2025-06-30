package juego;

import ui.Consola;
import utils.ConvertirCoordenadas;
import utils.InputAyuda;

public class PruebaJuego {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA INTEGRAL DEL JUEGO ===");

        // 1. Inicialización del juego
        Tablero tablero = new Tablero();
        Jugador jugadorRojo = new Jugador("Rojo");
        Jugador jugadorAzul = new Jugador("Azul");
        CirculoAccion circulo = new CirculoAccion();

        // 2. Configuración inicial
        tablero.colocarPiezasIniciales(jugadorRojo, jugadorAzul);
        Jugador jugadorActual = jugadorRojo;

        // 3. Mostrar estado inicial
        System.out.println("\n=== TABLERO INICIAL ===");
        Consola.mostrarTablero(tablero);

        // 4. Prueba de movimiento de la reina roja
        System.out.println("\n=== MOVIMIENTO REINA ROJA (A1->B2) ===");
        boolean resultadoMov1 = moverPieza(tablero, "A1", "B2", jugadorActual);
        Consola.mostrarTablero(tablero);

        // 5. Cambio de turno
        jugadorActual = jugadorAzul;

        // 6. Prueba de movimiento de la reina azul
        System.out.println("\n=== MOVIMIENTO REINA AZUL (F5->E5) ===");
        boolean resultadoMov2 = moverPieza(tablero, "F5", "E5", jugadorActual);
        Consola.mostrarTablero(tablero);

        // 7. Prueba de movimiento inválido
        System.out.println("\n=== MOVIMIENTO INVÁLIDO (E5->D6 - NO EXISTE) ===");
        boolean resultadoMov3 = moverPieza(tablero, "E5", "D6", jugadorActual);

        // 8. Resumen de pruebas
        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Movimiento reina roja (A1->B2): " + (resultadoMov1 ? "ÉXITO" : "FALLÓ"));
        System.out.println("Movimiento reina azul (F5->E5): " + (resultadoMov2 ? "ÉXITO" : "FALLÓ"));
        System.out.println("Movimiento inválido (E5->D6): " + (!resultadoMov3 ? "CORRECTO (falló como se esperaba)" : "ERROR (debió fallar)"));

        // 9. Prueba del círculo de acción
        System.out.println("\n=== PRUEBA CÍRCULO DE ACCIÓN ===");
        probarCirculoAccion(circulo, jugadorRojo, jugadorAzul);
    }

    private static boolean moverPieza(Tablero tablero, String origen, String destino, Jugador jugador) {
        System.out.println("Jugador " + jugador.getColor() + " intenta mover de " + origen + " a " + destino);

        int[] coordOrigen = ConvertirCoordenadas.desdeNotacionTablero(origen);
        int[] coordDestino = ConvertirCoordenadas.desdeNotacionTablero(destino);

        if (coordOrigen == null || coordDestino == null) {
            System.out.println("Coordenadas inválidas");
            return false;
        }

        Pieza pieza = tablero.obtenerPieza(coordOrigen[0], coordOrigen[1]);
        if (pieza == null || !pieza.getJugador().equals(jugador)) {
            System.out.println("No hay pieza del jugador en la posición de origen");
            return false;
        }

        return tablero.moverPieza(coordOrigen[0], coordOrigen[1],
                coordDestino[0], coordDestino[1]);
    }

    private static void probarCirculoAccion(CirculoAccion circulo, Jugador rojo, Jugador azul) {
        System.out.println("Acciones disponibles para Rojo:");
        System.out.println(circulo.obtenerAccionesDisponibles(rojo));

        System.out.println("\nAcciones disponibles para Azul:");
        System.out.println(circulo.obtenerAccionesDisponibles(azul));

        System.out.println("\nAvanzando fichas...");
        circulo.moverFicha(rojo, 0, 2); // Mueve primera ficha del rojo
        circulo.moverFicha(azul, 1, 3); // Mueve segunda ficha del azul

        System.out.println("\nAcciones después de mover:");
        System.out.println("Rojo: " + circulo.obtenerAccionesDisponibles(rojo));
        System.out.println("Azul: " + circulo.obtenerAccionesDisponibles(azul));
    }
}

package Modelo;

import java.util.ArrayList;

public class CirculoAcciones {
    private ArrayList<Accion> casillas;

    public CirculoAcciones() {
        casillas = new ArrayList<>();
        casillas.add(new Accion("Transicion", "Inicio"));
        casillas.add(new Accion("TomarCarta", "Tomar Carta 1"));
        casillas.add(new Accion("Movimiento", "Movimiento 1"));
        casillas.add(new Accion("Ataque", "Ataque 1"));
        casillas.add(new Accion("Rotar", "Rotación 1"));
        casillas.add(new Accion("Movimiento", "Movimiento 2"));
        casillas.add(new Accion("TomarCarta", "Tomar Carta 2"));
        casillas.add(new Accion("TomarCarta", "Tomar Carta 3"));
        casillas.add(new Accion("Ataque", "Ataque 2"));
        casillas.add(new Accion("Ataque", "Ataque 3"));
        casillas.add(new Accion("Rotar", "Rotación 2"));
        casillas.add(new Accion("Movimiento", "Movimiento 3"));
        casillas.add(new Accion("TomarCarta", "Tomar Carta 4"));
        casillas.add(new Accion("Transicion", "Fin"));
    }

    public Accion getCasilla(int posicion) {
        return casillas.get(posicion % casillas.size());
    }

    public String getTipoAccion(int posicion) {
        return getCasilla(posicion).getTipo();
    }

    public int getTamano() {
        return casillas.size();
    }

    public int avanzarFicha(int posicionActual, int pasos, Jugador jugador) {
        int nuevaPos = (posicionActual + pasos) % casillas.size();

        if (posicionActual == 12 && nuevaPos == 13) {
            return nuevaPos;
        }

        for (int i = 0; i < 3; i++) {
            if (jugador.getFichasAccion()[i] == nuevaPos) {
                throw new IllegalStateException("Ya tiene una ficha en esta posición.");
            }
        }

        return nuevaPos;
    }

    public void imprimirCirculoConFichas(int[] posiciones) {
        for (int i = 0; i < casillas.size(); i++) {
            String texto = String.format("%2d - %-15s", i, casillas.get(i).getNombre());
            for (int j = 0; j < posiciones.length; j++) {
                if (posiciones[j] == i) {
                    texto += " [F" + (j + 1) + "]";
                }
            }
            System.out.println(texto);
        }
    }

    public void mostrarAccionesDisponibles(int posicionActual) {
        System.out.println("\nPróximas 3 acciones disponibles:");
        for (int i = 1; i <= 3; i++) {
            int pos = (posicionActual + i) % 14;
            String accion = casillas.get(pos).getNombre();
            System.out.println(i + ". " + (pos == 13 ? "FIN → IR AL INICIO" : accion));
        }
    }

    public void mostrarProximasAcciones(int posicionActual) {
        System.out.println("\nACCIONES DISPONIBLES (3 siguientes):");
        for (int i = 1; i <= 3; i++) {
            int pos = (posicionActual + i) % casillas.size();
            Accion accion = casillas.get(pos);

            if (pos == 13) {
                System.out.println(i + ". IR AL INICIO (Fin → Transición)");
            } else {
                System.out.printf("%d. %-15s (Tipo: %s)\n",
                        i,
                        accion.getNombre(),
                        accion.getTipo());
            }
        }
    }


}


package juego;

import java.util.Collections;
import java.util.Stack;

public class Mazo {
    private Stack<Carta> cartas;
    private String tipo;

    public Mazo(String tipo) {
        this.tipo = tipo;
        this.cartas = new Stack<>();
        if(!tipo.startsWith("DESCARTE")) {
            inicializarMazo();
        }
    }

    private void inicializarMazo() {
        String[] efectos = new String[0];

        if (tipo.equals("MOVIMIENTO")) {
            efectos = new String[]{
                    "Mover 1 espacio horizontal",
                    "Mover 1 espacio vertical",
                    "Mover 1 espacio diagonal",
                    "Mover 1 espacio cualquier dirección",
                    "Mover 1 espacio horizontal",
                    "Mover 1 espacio vertical",
                    "Mover 1 espacio diagonal",
                    "Mover 1 espacio cualquier dirección"
            };
        } else if (tipo.equals("ATAQUE")) {
            efectos = new String[]{
                    "Ataque horizontal adyacente",
                    "Ataque vertical adyacente",
                    "Ataque diagonal adyacente",
                    "Ataque a cualquier espacio adyacente",
                    "Ataque horizontal adyacente",
                    "Ataque vertical adyacente",
                    "Ataque diagonal adyacente",
                    "Ataque a cualquier espacio adyacente"
            };
        }

        for(String efecto : efectos) {
            cartas.push(new Carta(tipo, efecto));
        }

        barajar();
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta tomarCarta() {
        return cartas.isEmpty() ? null : cartas.pop();
    }

    public void agregarCarta(Carta carta) {
        if(carta != null && carta.getTipo().equals(this.tipo.replace("DESCARTE_", ""))) {
            cartas.push(carta);
        }
    }

    public int cartasRestantes() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }
}

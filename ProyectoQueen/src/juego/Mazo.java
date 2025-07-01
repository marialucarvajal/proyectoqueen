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
        String[] efectos = tipo.equals("MOVIMIENTO") ?
                new String[]{
                        "Mover 1 espacio horizontal/vertical",
                        "Mover 1 espacio diagonal",
                        "Mover 2 espacios horizontales",
                        "Mover 2 espacios verticales",
                        "Mover en L como caballo",
                        "Mover 3 espacios en línea recta",
                        "Mover 1 espacio cualquier dirección",
                        "Mover 2 espacios en diagonal"
                } :
                new String[]{
                        "Atacar adyacente horizontal",
                        "Atacar adyacente vertical",
                        "Atacar en diagonal",
                        "Atacar a distancia 2 horizontal",
                        "Atacar a distancia 2 vertical",
                        "Atacar en cruz (3 espacios)",
                        "Ataque en X (diagonales)",
                        "Ataque en área 3x3"
                };

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
        if(carta != null &&
                (carta.getTipo().equals(this.tipo) ||
                        this.tipo.startsWith("DESCARTE"))) {
            cartas.add(carta);
        }
    }

    public int cartasRestantes() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }
}

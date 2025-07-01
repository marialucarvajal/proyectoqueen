package juego;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class Mazo {
    private Stack<Carta> cartas;
    private Stack<Carta> descarte;
    private String tipo;

    public Mazo(String tipo) {
        this.tipo = tipo;
        this.cartas = new Stack<>();
        this.descarte = new Stack<>();
        inicializarMazo();
    }

    private void inicializarMazo() {

        String[] efectosMovimiento = {
                "Mover 1 espacio horizontal/vertical",
                "Mover 1 espacio diagonal",
                "Mover 2 espacios horizontales",
                "Mover 2 espacios verticales",
                "Mover en L como caballo",
                "Mover 3 espacios en línea recta",
                "Mover 1 espacio cualquier dirección",
                "Mover 2 espacios en diagonal"
        };

        String[] efectosAtaque = {
                "Atacar adyacente horizontal",
                "Atacar adyacente vertical",
                "Atacar en diagonal",
                "Atacar a distancia 2 horizontal",
                "Atacar a distancia 2 vertical",
                "Atacar en cruz (3 espacios)",
                "Ataque en X (diagonales)",
                "Ataque en área 3x3"
        };

        Collections.shuffle(Arrays.asList(efectosMovimiento));
        Collections.shuffle(Arrays.asList(efectosAtaque));

        for (int i = 0; i < 8; i++) {
            String efecto = tipo.equals("MOVIMIENTO")
                    ? efectosMovimiento[i]
                    : efectosAtaque[i];
            cartas.push(new Carta(tipo, efecto));
        }
    }

    public Carta tomarCarta() {
        if (cartas.isEmpty()) {
            reiniciarMazo();
        }
        return cartas.pop();
    }

    public void descartar(Carta carta) {
        descarte.push(carta);
    }

    private void reiniciarMazo() {
        cartas.addAll(descarte);
        descarte.clear();
        Collections.shuffle(cartas);
    }

    public int cartasRestantes() {
        return cartas.size();
    }
}

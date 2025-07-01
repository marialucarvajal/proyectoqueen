package juego;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

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
        List<String> efectosMovimiento = new ArrayList<>(Arrays.asList(
                "Mover 1 espacio horizontal/vertical",
                "Mover 1 espacio diagonal",
                "Mover 2 espacios horizontales",
                "Mover 2 espacios verticales",
                "Mover en L como caballo",
                "Mover 3 espacios en línea recta",
                "Mover 1 espacio cualquier dirección",
                "Mover 2 espacios en diagonal",
                "Mover y rotar pieza",
                "Mover en zigzag (1h+1v)",
                "Mover como torre (horizontal/vertical ilimitado)",
                "Mover como alfil (diagonal ilimitado)"
        ));

        List<String> efectosAtaque = new ArrayList<>(Arrays.asList(
                "Atacar adyacente horizontal",
                "Atacar adyacente vertical",
                "Atacar en diagonal",
                "Atacar a distancia 2 horizontal",
                "Atacar a distancia 2 vertical",
                "Atacar en cruz (3 espacios)",
                "Ataque en X (diagonales)",
                "Ataque en área 3x3",
                "Ataque penetrante (ignora defensas)",
                "Ataque y retroceso",
                "Ataque silencioso (no consume turno)",
                "Ataque crítico (doble daño)"
        ));

        Collections.shuffle(efectosMovimiento);
        Collections.shuffle(efectosAtaque);

        List<String> efectos = tipo.equals("MOVIMIENTO") ?
                efectosMovimiento.subList(0, 8) :
                efectosAtaque.subList(0, 8);

        for (String efecto : efectos) {
            cartas.push(new Carta(tipo, efecto));
        }

        Collections.shuffle(cartas);
    }

    public Carta tomarCarta() {
        if (cartas.isEmpty()) {
            if (descarte.isEmpty()) {
                return null;
            }
            reiniciarMazo();
        }
        return cartas.pop();
    }

    public void descartar(Carta carta) {
        if (carta != null) {
            descarte.push(carta);
        }
    }

    private void reiniciarMazo() {
        Collections.shuffle(descarte);
        cartas.addAll(descarte);
        descarte.clear();
    }

    public int cartasRestantes() {
        return cartas.size() + descarte.size();
    }

    public void agregarCarta(Carta carta) {
        if (carta != null && carta.getTipo().equals(this.tipo)) {
            descarte.push(carta);
        }
    }

    public void reiniciarMazoCompleto() {
        cartas.clear();
        descarte.clear();
        inicializarMazo();
    }
}

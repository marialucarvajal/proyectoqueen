package juego;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String color;
    private List<Pieza> piezas;
    private List<Carta> cartas;

    public Jugador(String color) {
        this.color = color;
        this.piezas = new ArrayList<>();
        this.cartas = new ArrayList<>();
    }

    public void agregarPieza(Pieza pieza) {
        piezas.add(pieza);
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public String getColor() {
        return color;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public int getCartasCount() {
        return cartas.size();
    }

    public boolean tieneCartasMovimiento() {
        return cartas.stream().anyMatch(c -> c.getTipo().equals("MOVIMIENTO"));
    }

    public List<Carta> getCartasMovimiento() {
        List<Carta> cartasMov = new ArrayList<>();
        for (Carta c : cartas) {
            if (c.getTipo().equals("MOVIMIENTO")) {
                cartasMov.add(c);
            }
        }
        return cartasMov;
    }
}
package juego;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String color;
    private List<Pieza> piezas;
    private List<Carta> cartasMovimiento;
    private List<Carta> cartasAtaque;

    public Jugador(String color) {
        this.color = color;
        this.piezas = new ArrayList<>();
        this.cartasMovimiento = new ArrayList<>();
        this.cartasAtaque = new ArrayList<>();
    }

    public void agregarPieza(Pieza pieza) {
        if (pieza != null) {
            piezas.add(pieza);
        }
    }

    public List<Pieza> getPiezas() {
        return new ArrayList<>(piezas);
    }

    public boolean tienePieza(Pieza pieza) {
        return piezas.contains(pieza);
    }

    public void agregarCartaMovimiento(Carta carta) {
        if(carta != null && carta.getTipo().equals("MOVIMIENTO")) {
            cartasMovimiento.add(carta);
        }
    }

    public void agregarCartaAtaque(Carta carta) {
        if(carta != null && carta.getTipo().equals("ATAQUE")) {
            cartasAtaque.add(carta);
        }
    }

    public void usarCartaMovimiento(Carta carta) {
        cartasMovimiento.remove(carta);
    }

    public void usarCartaAtaque(Carta carta) {
        cartasAtaque.remove(carta);
    }

    public List<Carta> getCartasMovimiento() {
        return new ArrayList<>(cartasMovimiento);
    }

    public List<Carta> getCartasAtaque() {
        return new ArrayList<>(cartasAtaque);
    }

    public int getCartasCount() {
        return cartasMovimiento.size();
    }

    public int getCartasAtaqueCount() {
        return cartasAtaque.size();
    }

    public String getColor() {
        return color;
    }

    public void agregarCarta(Carta carta) {
        if (carta == null) {
            System.err.println("Error: Carta nula");
            return;
        }

        if (carta.getTipo().equals("MOVIMIENTO")) {
            cartasMovimiento.add(carta);
        }
        else if (carta.getTipo().equals("ATAQUE")) {
            cartasAtaque.add(carta);
        }
        else {
            System.err.println("Tipo de carta no válido: " + carta.getTipo());
        }
    }

}
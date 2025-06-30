package modelo;

import modelo.cartas.Carta;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jugador {
    private String nombre;
    private String color;
    private int[] fichas;
    private List<Carta> cartas;

    public Jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.fichas = new int[]{0, 0, 0};
        this.cartas = new ArrayList<>();
    }

    public void tomarCarta(MazoCartas mazo) {
        if (mazo != null) {
            Carta carta = mazo.tomarCarta();
            if (carta != null) {
                cartas.add(carta);
            }
        }
    }

    public boolean usarCartaAtaque() {
        return cartas.removeIf(c -> c.getTipo().equals("Ataque"));
    }

    public List<Carta> getCartasAtaque() {
        return cartas.stream()
                .filter(c -> c.getTipo().equals("Ataque"))
                .collect(Collectors.toList());
    }

    public void removerCarta(int indice) {
        if (indice >= 0 && indice < cartas.size()) {
            cartas.remove(indice);
        }
    }

    public void avanzarFicha(int fichaIndex, int pasos, int tamanoCirculo) {
        if (fichaIndex >= 0 && fichaIndex < 3) {
            fichas[fichaIndex] = (fichas[fichaIndex] + pasos) % tamanoCirculo;
        }
    }

    public void rotarFichas(boolean sentidoHorario) {
        if (sentidoHorario) {
            int temp = fichas[2];
            fichas[2] = fichas[1];
            fichas[1] = fichas[0];
            fichas[0] = temp;
        } else {
            int temp = fichas[0];
            fichas[0] = fichas[1];
            fichas[1] = fichas[2];
            fichas[2] = temp;
        }
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public String getColorContrario() {
        return color.equals("rojo") ? "azul" : "rojo";
    }

    public int[] getFichas() {
        return fichas.clone();
    }

    public int getFichaActiva() {
        return fichas[0];
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }

    public int contarCartas() {
        return cartas.size();
    }

    public int contarCartasAtaque() {
        return (int) cartas.stream()
                .filter(c -> c.getTipo().equals("Ataque"))
                .count();
    }

    public void mostrarCartas() {
        if (cartas.isEmpty()) {
            System.out.println("No tienes cartas");
            return;
        }

        System.out.println("Tus cartas:");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println((i + 1) + ". " + cartas.get(i).getDescripcion());
        }
    }

    public Carta getCarta(int indice) {
        if (indice >= 0 && indice < cartas.size()) {
            return cartas.get(indice);
        }
        return null;
    }
}

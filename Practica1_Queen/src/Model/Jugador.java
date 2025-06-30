package Model;

import java.util.ArrayList;

public class Jugador {

    private String nombre;
    private String color;
    private int[] fichasAccion;
    private ArrayList<Carta> cartas;

    public Jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.fichasAccion = new int[]{0, 0, 0};
        this.cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public int contarCartasAtaque() {
        int count = 0;
        for (Carta c : cartas) {
            if (c.getTipo().equals("Ataque")) count++;
        }
        return count;
    }

    public boolean usarCartaAtaque() {
        for (Carta c : cartas) {
            if (c.getTipo().equals("Ataque")) {
                cartas.remove(c);
                return true;
            }
        }
        return false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public int[] getFichasAccion() {
        return fichasAccion;
    }

    public boolean avanzarFicha(int numeroFicha, int pasos, int tamanoCirculo) {
        int nuevaPos = (fichasAccion[numeroFicha] + pasos) % tamanoCirculo;
        for (int i = 0; i < 3; i++) {
            if (i != numeroFicha && fichasAccion[i] == nuevaPos) {
                return false;
            }
        }
        fichasAccion[numeroFicha] = nuevaPos;
        return true;
    }

    public void removerCarta(int indice) {
        if (indice >= 0 && indice < cartas.size()) {
            cartas.remove(indice);
        }
    }

    public void mostrarCartas() {
        if (cartas.isEmpty()) {
            System.out.println("No tienes cartas.");
            return;
        }
        System.out.println("Tus cartas: ");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println((i + 1) + ". " + cartas.get(i).getDescripcion() + " (" + cartas.get(i).getTipo() + ")");
        }
    }

    public Carta getCarta(int indice) {
        return (indice >= 0 && indice < cartas.size()) ? cartas.get(indice) : null;
    }

    public boolean puedeLlegarAlFinal(int fichaIndex, int tamanoCirculo) {
        int posicionActual = fichasAccion[fichaIndex];
        return (posicionActual == 12);
    }

    public int contarCartasPorTipo(String tipo) {
        return (int) cartas.stream()
                .filter(c -> c.getTipo().equals(tipo))
                .count();
    }

    public void mostrarEstadoCartas() {
        System.out.printf("\nCARTAS: Movimiento (%d) | Ataque (%d)\n",
                contarCartasPorTipo("Movimiento"),
                contarCartasPorTipo("Ataque"));
    }

    public boolean usarCarta(String tipo) {
        Iterator<Carta> it = cartas.iterator();
        while (it.hasNext()) {
            Carta c = it.next();
            if (c.getTipo().equals(tipo)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
}

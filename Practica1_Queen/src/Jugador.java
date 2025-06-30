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

    public int contarCartas() {
        return cartas.size();
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

    public void avanzarFicha(int numeroFicha, int pasos, int tamanoCirculo) {
        if (numeroFicha >= 0 && numeroFicha < 3) {
            fichasAccion[numeroFicha] = (fichasAccion[numeroFicha] + pasos) % tamanoCirculo;
        }
    }
}

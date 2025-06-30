import java.util.*;

public class MazoCartas {

    private Stack<Carta> mazo;
    private Stack<Carta> descarte;
    private String tipo;

    public MazoCartas(String tipo) {
        this.tipo = tipo;
        this.mazo = new Stack<>();
        this.descarte = new Stack<>();
        inicializarCartas();
    }

    private void inicializarCartas() {
        List<Carta> cartasTemp = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            if (tipo.equals("Movimiento")) {
                cartasTemp.add(new MovimientoCarta("Mover como patrón #" + i));
            } else if (tipo.equals("Ataque")) {
                cartasTemp.add(new AtaqueCarta("Atacar como patrón #" + i));
            }
        }
        Collections.shuffle(cartasTemp);
        mazo.addAll(cartasTemp);
    }

    public Carta tomarCarta() {
        if (mazo.isEmpty() && descarte.isEmpty()) {
            System.out.println("No hay cartas disponibles.");
            return null;
        }
        if (mazo.isEmpty()) {
            System.out.println("Barajando cartas descartadas...");
            mazo.addAll(descarte);
            descarte.clear();
            Collections.shuffle(mazo);
        }
        return mazo.pop();
    }

    public void descartarCarta(Carta carta) {
        descarte.push(carta);
    }

    public int cartasRestantes() {
        return mazo.size();
    }

    public int cartasDescartadas() {
        return descarte.size();
    }

    public void reiniciarMazoSiNecesario() {
        if (mazo.isEmpty()) {
            System.out.println("Mezclando mazo de descarte...");
            mazo.addAll(descarte);
            descarte.clear();
            Collections.shuffle(mazo);
        }
    }
}



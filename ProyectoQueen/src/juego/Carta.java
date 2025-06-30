package juego;

public class Carta {
    private String tipo;
    private String efecto;

    public Carta(String tipo, String efecto) {
        this.tipo = tipo;
        this.efecto = efecto;
    }

    public String getTipo() {
        return tipo;
    }
    public String getEfecto() {
        return efecto;
    }

    @Override
    public String toString() {
        return tipo + ": " + efecto;
    }
}

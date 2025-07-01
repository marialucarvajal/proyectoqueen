package juego;

public class Pieza {
    private Jugador jugador;
    private boolean esReina;

    public Pieza(Jugador jugador, boolean esReina) {
        this.jugador = jugador;
        this.esReina = esReina;
        jugador.agregarPieza(this);
    }

    public boolean esReina() {
        return esReina;
    }

    public Jugador getJugador() {
        return jugador;
    }
}

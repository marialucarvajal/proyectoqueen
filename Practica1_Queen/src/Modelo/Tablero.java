class Tablero {
    public void mostrar() {
        System.out.println("Tablero mostrado");
    }

    public boolean moverPieza(String origen, String destino, String color) {
        System.out.println("Moviendo pieza de " + origen + " a " + destino);
        return true;
    }

    public boolean atacar(String coordenada, String color) {
        System.out.println("Atacando coordenada " + coordenada);
        return true;
    }

    public boolean reinaEliminada(String color) {
        return false;
    }
}
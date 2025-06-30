package modelo.cartas;

class MazoCartas {

    private String tipo;

    public MazoCartas(String tipo) {
        this.tipo = tipo;
    }

    public Carta tomar() {
        System.out.println("Tomando carta de " + tipo);
        return new Carta(tipo);
    }

    public int cartasRestantes() {
        return 5; // Valor de ejemplo
    }
}



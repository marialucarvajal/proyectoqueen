class Jugador {
    private String nombre;
    private String color;
    private int[] fichas = new int[3];
    private java.util.List<Carta> cartas = new java.util.ArrayList<>();

    public Jugador(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public void tomarCarta(MazoCartas mazo) {
        Carta carta = mazo.tomar();
        if (carta != null) cartas.add(carta);
    }

    public void rotarFichas() {
        int temp = fichas[0];
        fichas[0] = fichas[1];
        fichas[1] = fichas[2];
        fichas[2] = temp;
    }

    public String getNombre() { return nombre; }
    public String getColor() { return color; }
    public String getColorContrario() { return color.equals("rojo") ? "azul" : "rojo"; }
    public int[] getFichas() { return fichas; }
    public int getFichaActiva() { return fichas[0]; }
    public java.util.List<Carta> getCartas() { return cartas; }
}


class CirculoAcciones {

    private String[] acciones = {
            "Inicio", "TomarCarta", "Movimiento", "Ataque",
            "Rotar", "Movimiento", "TomarCarta", "TomarCarta",
            "Ataque", "Ataque", "Rotar", "Movimiento",
            "TomarCarta", "Fin"
    };

    public String avanzarFicha(Jugador jugador, int ficha, int pasos) {
        int posicion = (jugador.getFichas()[ficha] + pasos) % 14;
        jugador.getFichas()[ficha] = posicion;
        return acciones[posicion];
    }

    public void mostrarSiguientesAcciones(int posicionActual) {
        for (int i = 1; i <= 3; i++) {
            System.out.println(i + ". " + acciones[(posicionActual + i) % 14]);
        }
    }

    public void mostrarPosiciones(int[] fichas) {
        for (int i = 0; i < fichas.length; i++) {
            System.out.println("Ficha " + (i+1) + ": " + acciones[fichas[i]]);
        }
    }
}


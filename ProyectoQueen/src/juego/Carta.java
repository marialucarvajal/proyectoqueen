package juego;

public class Carta {
    private String tipo;  // MOVIMIENTO o ATAQUE
    private String efecto;
    private int costoEnergia;

    public Carta(String tipo, String efecto) {
        this.tipo = tipo;
        this.efecto = efecto;
        this.costoEnergia = calcularCostoEnergia();
    }

    private int calcularCostoEnergia() {
        if (efecto.contains("ilimitado") || efecto.contains("3x3")) {
            return 3;
        } else if (efecto.contains("2") || efecto.contains("doble")) {
            return 2;
        }
        return 1;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEfecto() {
        return efecto;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (Energía: %d)", tipo, efecto, costoEnergia);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carta otra = (Carta) obj;
        return tipo.equals(otra.tipo) && efecto.equals(otra.efecto);
    }

    @Override
    public int hashCode() {
        return tipo.hashCode() + efecto.hashCode();
    }
}

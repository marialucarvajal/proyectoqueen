package juego;

public class Carta {
    private String tipo;  // MOVIMIENTO o ATAQUE
    private String efecto;
    private int energia;

    public Carta(String tipo, String efecto) {
        this.tipo = tipo;
        this.efecto = efecto;
        this.energia = calcularCostoEnergia();
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

    public int getEnergia() {
        return energia;
    }

    @Override
    public String toString() {
        return "[MOVIMIENTO] " + efecto + " (Energía: " + energia + ")";
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

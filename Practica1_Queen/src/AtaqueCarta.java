import Model.Carta;

public class AtaqueCarta extends Carta {
    public AtaqueCarta(String descripcion) {
        super(descripcion);
    }

    @Override
    public String getTipo() {
        return "Ataque";
    }
}
public class Baloncesto extends Encuentro {
    public Baloncesto(Equipo equipoA, Equipo equipoB) {
        super(equipoA, equipoB);
    }

    @Override
    protected void validarMarcador(int a, int b) {
        super.validarMarcador(a, b);
        if (a == b) {
            throw new IllegalArgumentException("En baloncesto no se permiten empates.");
        }
    }

    @Override
    protected int[] calcularPuntos(int a, int b) {
        return a > b ? new int[] {2, 1} : new int[] {1, 2};
    }

    @Override
    public String getDescripcion() {
        String descripcion = "Baloncesto: " + getEquipoA().getNombre()
                + " vs " + getEquipoB().getNombre();
        return getMarcadorA() == null ? descripcion
                : descripcion + " | " + getMarcadorA() + " - " + getMarcadorB();
    }
}

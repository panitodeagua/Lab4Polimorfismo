public class FutbolSala extends Encuentro {
    public FutbolSala(Equipo equipoA, Equipo equipoB) {
        super(equipoA, equipoB);
    }

    @Override
    protected int[] calcularPuntos(int a, int b) {
        if (a == b) { return new int[] {1, 1}; }
        return a > b ? new int[] {3, 0} : new int[] {0, 3};
    }

    @Override
    public String getDescripcion() {
        String descripcion = "Fútbol sala: " + getEquipoA().getNombre()
                + " vs " + getEquipoB().getNombre();
        return getMarcadorA() == null ? descripcion
                : descripcion + " | " + getMarcadorA() + " - " + getMarcadorB();
    }
}

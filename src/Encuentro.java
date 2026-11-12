import java.util.Set;

/** Flujo común de los deportes; las reglas se resuelven por polimorfismo. */
public abstract class Encuentro implements Programable, Puntuable {
    private final Equipo equipoA;
    private final Equipo equipoB;
    private Programacion programacion;
    private Integer marcadorA;
    private Integer marcadorB;

    protected Encuentro(Equipo equipoA, Equipo equipoB) {
        if (equipoA == null || equipoB == null) {
            throw new IllegalArgumentException("Ambos equipos son obligatorios.");
        }
        if (equipoA.getCodigo().equals(equipoB.getCodigo())) {
            throw new IllegalArgumentException("Los equipos deben ser distintos.");
        }
        this.equipoA = equipoA;
        this.equipoB = equipoB;
    }

    public final Equipo getEquipoA() { return equipoA; }
    public final Equipo getEquipoB() { return equipoB; }
    public final Integer getMarcadorA() { return marcadorA; }
    public final Integer getMarcadorB() { return marcadorB; }
    @Override
    public final Programacion getProgramacion() { return programacion; }

    @Override
    public final void programar(Programacion propuesta, Copa copa) {
        if (marcadorA != null) {
            throw new IllegalStateException("Un encuentro finalizado no se puede reprogramar.");
        }
        if (copa == null) {
            throw new IllegalArgumentException("La copa es obligatoria.");
        }
        copa.validarProgramacion(this, propuesta);
        programacion = propuesta;
    }

    @Override
    public final Set<String> getCodigosEquipos() {
        return Set.of(equipoA.getCodigo(), equipoB.getCodigo());
    }

    @Override
    public final String getEstado() {
        if (marcadorA != null) { return "FINALIZADO"; }
        return programacion == null ? "SIN_PROGRAMAR" : "PROGRAMADO";
    }

    @Override
    public final void registrarResultado(int a, int b) {
        if (!getEstado().equals("PROGRAMADO")) {
            throw new IllegalStateException("Solo un encuentro programado puede recibir resultado.");
        }
        validarMarcador(a, b);
        int[] puntos = calcularPuntos(a, b);
        if (puntos == null || puntos.length != 2 || puntos[0] < 0 || puntos[1] < 0) {
            throw new IllegalArgumentException("El deporte debe devolver dos incrementos no negativos.");
        }
        // Verificar AMBAS sumas antes de modificar cualquier equipo.
        Math.addExact(equipoA.getPuntosTabla(), puntos[0]);
        Math.addExact(equipoB.getPuntosTabla(), puntos[1]);
        equipoA.sumarPuntos(puntos[0]);
        equipoB.sumarPuntos(puntos[1]);
        marcadorA = a;
        marcadorB = b;
    }

    protected void validarMarcador(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Los marcadores deben ser enteros no negativos.");
        }
    }

    protected abstract int[] calcularPuntos(int a, int b);
    @Override
    public abstract String getDescripcion();
}

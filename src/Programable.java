import java.util.Set;

/** Capacidad de participar en el calendario, independiente de los resultados. */
public interface Programable {
    Programacion getProgramacion();
    void programar(Programacion propuesta, Copa copa);
    Set<String> getCodigosEquipos();
    String getDescripcion();
    String getEstado();
}

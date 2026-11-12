import java.time.LocalDate;

/** Valor inmutable; concentra el único criterio cronológico del sistema. */
public final class Programacion implements Comparable<Programacion> {
    private final LocalDate fecha;
    private final int hora;
    private final String sede;

    public Programacion(LocalDate fecha, int hora, String sede) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23.");
        }
        if (sede == null || sede.trim().isEmpty()) {
            throw new IllegalArgumentException("La sede no puede estar vacía.");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
    }

    public LocalDate getFecha() { return fecha; }
    public int getHora() { return hora; }
    public String getSede() { return sede; }

    @Override
    public int compareTo(Programacion otra) {
        int comparacion = fecha.compareTo(otra.fecha);
        if (comparacion != 0) { return comparacion; }
        comparacion = Integer.compare(hora, otra.hora);
        return comparacion != 0 ? comparacion : sede.compareTo(otra.sede);
    }
}

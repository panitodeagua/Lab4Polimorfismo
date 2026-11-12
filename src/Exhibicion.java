import java.util.Set;

public class Exhibicion implements Programable {
    private final String titulo;
    private Programacion programacion;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public String getTitulo() { return titulo; }
    @Override
    public Programacion getProgramacion() { return programacion; }

    @Override
    public void programar(Programacion propuesta, Copa copa) {
        if (copa == null) {
            throw new IllegalArgumentException("La copa es obligatoria.");
        }
        copa.validarProgramacion(this, propuesta);
        programacion = propuesta;
    }

    @Override
    public Set<String> getCodigosEquipos() { return Set.of(); }
    @Override
    public String getDescripcion() { return "Exhibición: " + titulo; }
    @Override
    public String getEstado() {
        return programacion == null ? "SIN_PROGRAMAR" : "PROGRAMADA";
    }
}

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/** Registro de equipos y eventos, conflictos y consultas de la copa. */
public class Copa implements Iterable<Programable> {
    private final ArrayList<Equipo> equipos;
    private final ArrayList<Programable> eventos;
    private final HashMap<Integer, Puntuable> puntuables;

    public Copa() {
        equipos = new ArrayList<>();
        eventos = new ArrayList<>();
        puntuables = new HashMap<>();
    }

    public void registrarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo es obligatorio.");
        }
        for (Equipo registrado : equipos) {
            if (registrado.getCodigo().equals(equipo.getCodigo())) {
                throw new IllegalArgumentException("El código ya está registrado.");
            }
        }
        equipos.add(equipo);
    }

    public Equipo buscarEquipo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        for (Equipo equipo : equipos) {
            if (equipo.getCodigo().equals(codigo.trim())) { return equipo; }
        }
        throw new IllegalArgumentException("No existe un equipo con ese código.");
    }

    public int agregarEncuentro(Encuentro encuentro) {
        validarEventoNuevo(encuentro);
        if (buscarEquipo(encuentro.getEquipoA().getCodigo()) != encuentro.getEquipoA()
                || buscarEquipo(encuentro.getEquipoB().getCodigo()) != encuentro.getEquipoB()) {
            throw new IllegalArgumentException("Use las referencias de los equipos registrados.");
        }
        eventos.add(encuentro);
        int id = eventos.size();
        puntuables.put(id, encuentro);
        return id;
    }

    public int agregarEvento(Programable evento) {
        validarEventoNuevo(evento);
        // Esta ruta registra solo la capacidad de calendario.
        eventos.add(evento);
        return eventos.size();
    }

    private void validarEventoNuevo(Programable evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento es obligatorio.");
        }
        for (Programable registrado : eventos) {
            if (registrado == evento) {
                throw new IllegalArgumentException("El evento ya está registrado.");
            }
        }
        if (evento.getProgramacion() != null) {
            throw new IllegalArgumentException("El evento nuevo debe estar sin programar.");
        }
    }

    public Programable buscarEvento(int id) {
        if (id < 1 || id > eventos.size()) {
            throw new IllegalArgumentException("ID de evento inexistente.");
        }
        return eventos.get(id - 1);
    }

    public List<Programable> listarEventos() { return List.copyOf(eventos); }

    public void programarEvento(int id, LocalDate fecha, int hora, String sede) {
        Programable evento = buscarEvento(id);
        evento.programar(new Programacion(fecha, hora, sede), this);
    }

    public void validarProgramacion(Programable evento, Programacion propuesta) {
        boolean registrado = false;
        for (Programable actual : eventos) {
            if (actual == evento) { registrado = true; break; }
        }
        if (!registrado || propuesta == null) {
            throw new IllegalArgumentException("Se requiere un evento registrado y una programación válida.");
        }
        for (Programable otro : eventos) {
            if (otro == evento || otro.getProgramacion() == null) { continue; }
            Programacion vigente = otro.getProgramacion();
            boolean mismaHora = vigente.getFecha().equals(propuesta.getFecha())
                    && vigente.getHora() == propuesta.getHora();
            if (!mismaHora) { continue; }
            if (vigente.getSede().equals(propuesta.getSede())) {
                throw new IllegalArgumentException("La sede está ocupada en esa fecha y hora.");
            }
            if (!Collections.disjoint(evento.getCodigosEquipos(), otro.getCodigosEquipos())) {
                throw new IllegalArgumentException("Un equipo ya tiene otro evento en esa fecha y hora.");
            }
        }
    }

    public void registrarResultado(int id, int a, int b) {
        buscarEvento(id);
        Puntuable puntuable = puntuables.get(id);
        if (puntuable == null) {
            throw new IllegalArgumentException("El evento no admite resultados.");
        }
        puntuable.registrarResultado(a, b);
    }

    public List<Equipo> getTablaPosiciones() {
        ArrayList<Equipo> tabla = new ArrayList<>(equipos);
        Collections.sort(tabla);
        return List.copyOf(tabla);
    }

    @Override
    public Iterator<Programable> iterator() {
        ArrayList<Programable> calendario = new ArrayList<>();
        for (Programable evento : eventos) {
            if (evento.getProgramacion() != null) { calendario.add(evento); }
        }
        calendario.sort(Comparator.comparing(Programable::getProgramacion));
        return List.copyOf(calendario).iterator();
    }
}

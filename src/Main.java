import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Punto de entrada; la consola delega las reglas de negocio a Copa. */
public final class Main {
    private static Scanner scanner;
    private static Copa copa;

    private Main() { }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        copa = new Copa();
        try {
            cargarDatosIniciales();
            System.out.println("ArenaGT | Copa interempresas");
            System.out.println("Datos de demostración cargados: 4 equipos, 4 encuentros y 1 exhibición.");
            ejecutarMenu();
        } finally {
            scanner.close();
        }
    }

    private static void cargarDatosIniciales() {
        Equipo aurora = new Equipo("E01", "Aurora", "Empresa Norte");
        Equipo buhos = new Equipo("E02", "Búhos", "Empresa Sur");
        Equipo condores = new Equipo("E03", "Cóndores", "Empresa Norte");
        Equipo delfines = new Equipo("E04", "Delfines", "Empresa Sur");
        copa.registrarEquipo(aurora);
        copa.registrarEquipo(buhos);
        copa.registrarEquipo(condores);
        copa.registrarEquipo(delfines);

        int futbol1 = copa.agregarEncuentro(new FutbolSala(aurora, buhos));
        int basket1 = copa.agregarEncuentro(new Baloncesto(condores, delfines));
        int futbol2 = copa.agregarEncuentro(new FutbolSala(aurora, condores));
        int basket2 = copa.agregarEncuentro(new Baloncesto(buhos, delfines));
        int exhibicion = copa.agregarEvento(new Exhibicion("Demostración deportiva de apertura"));
        LocalDate fecha = LocalDate.of(2026, 11, 6);
        copa.programarEvento(exhibicion, fecha, 8, "Plaza central");
        copa.programarEvento(futbol1, fecha, 9, "Cancha 1");
        copa.programarEvento(basket1, fecha, 10, "Gimnasio");
        copa.programarEvento(futbol2, fecha.plusDays(1), 9, "Cancha 1");
        copa.programarEvento(basket2, fecha.plusDays(1), 10, "Gimnasio");
        copa.registrarResultado(futbol1, 2, 2);
        copa.registrarResultado(basket1, 60, 50);
        // Cóndores: 2; Búhos, Delfines y Aurora: 1, con desempate por nombre.
    }

    private static void ejecutarMenu() {
        boolean continuar = true;
        while (continuar) {
            try {
                mostrarMenu();
                int opcion = leerEntero("Opción: ");
                switch (opcion) {
                    case 1: registrarEquipo(); break;
                    case 2: crearEncuentro(); break;
                    case 3: crearExhibicion(); break;
                    case 4: programarEvento(); break;
                    case 5: registrarResultado(); break;
                    case 6: mostrarTabla(); break;
                    case 7: mostrarCalendario(); break;
                    case 0: continuar = false; break;
                    default: System.out.println("Rechazo: opción inexistente.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nFin de la entrada. Hasta luego.");
                return;
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
                System.out.println("Rechazo: " + e.getMessage());
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void mostrarMenu() {
        System.out.println("\n1. Registrar equipo");
        System.out.println("2. Crear encuentro");
        System.out.println("3. Crear exhibición");
        System.out.println("4. Programar / reprogramar evento");
        System.out.println("5. Registrar resultado");
        System.out.println("6. Mostrar tabla de posiciones");
        System.out.println("7. Mostrar calendario");
        System.out.println("0. Salir");
    }

    private static void registrarEquipo() {
        String codigo = leerTexto("Código: ");
        String nombre = leerTexto("Nombre: ");
        String empresa = leerTexto("Empresa: ");
        copa.registrarEquipo(new Equipo(codigo, nombre, empresa));
        System.out.println("Equipo registrado.");
    }

    private static void crearEncuentro() {
        mostrarTabla();
        int deporte = leerEntero("Deporte (1: fútbol sala, 2: baloncesto): ");
        if (deporte != 1 && deporte != 2) {
            throw new IllegalArgumentException("Deporte inexistente.");
        }
        Equipo equipoA = copa.buscarEquipo(leerTexto("Código del equipo A: "));
        Equipo equipoB = copa.buscarEquipo(leerTexto("Código del equipo B: "));
        Encuentro encuentro = deporte == 1 ? new FutbolSala(equipoA, equipoB)
                : new Baloncesto(equipoA, equipoB);
        int id = copa.agregarEncuentro(encuentro);
        System.out.println("Encuentro creado con ID " + id + ".");
    }

    private static void crearExhibicion() {
        int id = copa.agregarEvento(new Exhibicion(leerTexto("Título: ")));
        System.out.println("Exhibición creada con ID " + id + ".");
    }

    private static void programarEvento() {
        mostrarEventos();
        int id = leerEntero("ID del evento: ");
        LocalDate fecha = leerFecha("Fecha (aaaa-mm-dd): ");
        int hora = leerEntero("Hora (0 a 23): ");
        String sede = leerTexto("Sede: ");
        copa.programarEvento(id, fecha, hora, sede);
        System.out.println("Programación guardada.");
    }

    private static void registrarResultado() {
        mostrarEventos();
        int id = leerEntero("ID del encuentro: ");
        int a = leerEntero("Marcador A: ");
        int b = leerEntero("Marcador B: ");
        copa.registrarResultado(id, a, b);
        System.out.println("Resultado registrado y puntos actualizados.");
    }

    private static void mostrarEventos() {
        List<Programable> eventos = copa.listarEventos();
        for (int i = 0; i < eventos.size(); i++) {
            Programable evento = eventos.get(i);
            System.out.printf("%d | %s | %s%n", i + 1, evento.getDescripcion(), evento.getEstado());
        }
        if (eventos.isEmpty()) { System.out.println("No hay eventos registrados."); }
    }

    private static void mostrarTabla() {
        System.out.printf("%n%-8s %-22s %-22s %s%n", "Código", "Nombre", "Empresa", "Puntos");
        for (Equipo equipo : copa.getTablaPosiciones()) {
            System.out.printf("%-8s %-22s %-22s %d%n", equipo.getCodigo(), equipo.getNombre(),
                    equipo.getEmpresa(), equipo.getPuntosTabla());
        }
    }

    private static void mostrarCalendario() {
        boolean vacio = true;
        for (Programable evento : copa) {
            vacio = false;
            Programacion p = evento.getProgramacion();
            System.out.printf("%s %02d:00 | %s | %s | %s%n", p.getFecha(), p.getHora(),
                    p.getSede(), evento.getDescripcion(), evento.getEstado());
        }
        if (vacio) { System.out.println("No hay eventos programados."); }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Rechazo: ingrese un entero entre -2147483648 y 2147483647.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje));
            } catch (DateTimeParseException e) {
                System.out.println("Rechazo: use una fecha existente con formato aaaa-mm-dd.");
            }
        }
    }
}

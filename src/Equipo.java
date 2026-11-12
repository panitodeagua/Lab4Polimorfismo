/** Equipo participante y criterio de orden de la tabla general. */
public class Equipo implements Comparable<Equipo> {
    private final String codigo;
    private final String nombre;
    private final String empresa;
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        this.codigo = validarTexto(codigo, "Código");
        this.nombre = validarTexto(nombre, "Nombre");
        this.empresa = validarTexto(empresa, "Empresa");
        this.puntosTabla = 0;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return texto.trim();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public int getPuntosTabla() { return puntosTabla; }

    void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        }
        puntosTabla = Math.addExact(puntosTabla, puntos);
    }

    @Override
    public int compareTo(Equipo otro) {
        int comparacion = Integer.compare(otro.puntosTabla, puntosTabla);
        return comparacion != 0 ? comparacion : nombre.compareTo(otro.nombre);
    }
}

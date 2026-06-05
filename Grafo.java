/**
 * Grafo no dirigido de celdas del laberinto.
 * Representación: tabla propia con secuencias enlazadas manuales.
 *
 * Cada nodo es una Celda; las aristas conectan celdas transitables adyacentes.
 */
public class Grafo {

    // ── Adyacencia propia ─────────────────────────────────────────────────
    // Usamos TablaHash<Integer, ListaSimple<Celda>> donde la clave es
    // el índice lineal de la celda: fila * columnas + columna.
    private final TablaHash<Integer, ListaSimple<Celda>> adyacencia;
    private final int columnas;   // necesario para calcular la clave
    private int cantidadAristas;

    // ── Constructor privado, use desdeMatriz() ─────────────────────────────
    private Grafo(int columnas) {
        this.adyacencia    = new TablaHash<>();
        this.columnas      = columnas;
        this.cantidadAristas = 0;
    }

    // ── Clave lineal ───────────────────────────────────────────────────────
    private int clave(int fila, int col) {
        return fila * columnas + col;
    }

    // ── Agregar nodo (celda) ───────────────────────────────────────────────
    private void agregarNodo(Celda celda) {
        int k = clave(celda.getFila(), celda.getColumna());
        if (!adyacencia.containsKey(k)) {
            adyacencia.put(k, new ListaSimple<>());
        }
    }

    // ── Agregar arista no dirigida ─────────────────────────────────────────
    private void agregarArista(Celda a, Celda b) {
        int ka = clave(a.getFila(), a.getColumna());
        int kb = clave(b.getFila(), b.getColumna());
        adyacencia.get(ka).agregar(b);
        adyacencia.get(kb).agregar(a);
        cantidadAristas++;
    }

    // ── Vecinos de una celda ───────────────────────────────────────────────
    public ListaSimple<Celda> vecinos(Celda celda) {
        int k = clave(celda.getFila(), celda.getColumna());
        ListaSimple<Celda> lista = adyacencia.get(k);
        return lista != null ? lista : new ListaSimple<>();
    }

    public int getCantidadAristas() { return cantidadAristas; }

    // ── MÉTODO PRINCIPAL: desdeMatriz ──────────────────────────────────────
    /**
     * Construye un Grafo a partir de la matriz char[][] del MazeSnapshot.
     * Una arista se crea entre dos celdas adyacentes (4-direccional) si
     * ambas son transitables (no son PARED).
     *
     * @param laberinto  el laberinto cuya estructura se modelará como grafo
     * @return           Grafo con nodos y aristas listo para recorridos
     */
    public static Grafo desdeMatriz(Laberinto laberinto) {
        int filas    = laberinto.getFilas();
        int columnas = laberinto.getColumnas();
        Grafo grafo  = new Grafo(columnas);

        // Paso 1: agregar todos los nodos transitables
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Celda c = laberinto.getCelda(i, j);
                if (c.getTipo() != TipoCelda.PARED) {
                    grafo.agregarNodo(c);
                }
            }
        }

        // Paso 2: agregar aristas entre nodos adyacentes transitables
        int[] dFila = {-1, 1, 0, 0};
        int[] dCol  = {0, 0, -1, 1};

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Celda actual = laberinto.getCelda(i, j);
                if (actual.getTipo() == TipoCelda.PARED) continue;

                for (int d = 0; d < 4; d++) {
                    int ni = i + dFila[d];
                    int nj = j + dCol[d];
                    if (ni < 0 || ni >= filas || nj < 0 || nj >= columnas) continue;
                    Celda vecino = laberinto.getCelda(ni, nj);
                    if (vecino.getTipo() == TipoCelda.PARED) continue;

                    // Solo agregar arista en una dirección para evitar duplicados
                    // (procesamos cada par exactamente una vez: solo cuando vecino
                    //  tiene índice lineal mayor que actual)
                    if (grafo.clave(ni, nj) > grafo.clave(i, j)) {
                        grafo.agregarArista(actual, vecino);
                    }
                }
            }
        }

        return grafo;
    }

    // ── Estadísticas del grafo ─────────────────────────────────────────────
    public void imprimirEstadisticas() {
        System.out.println("+-- Grafo desde matriz ------------------------+");
        System.out.println("|  Nodos (celdas transitables): " + adyacencia.size());
        System.out.println("|  Aristas (conexiones validas): " + cantidadAristas);
        System.out.println("+----------------------------------------------+");
    }
}

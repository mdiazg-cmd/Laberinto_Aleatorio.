/**
 * Registro inmutable de una partida resuelta.
 * Se usa como valor en ArbolBinario&lt;Integer, Record&gt;
 * y como clave implícita para el ranking (longitud de camino, tiempo).
 *
 * Nota: se llamará "PartidaRecord" para no colisionar con java.lang.Record
 * del preview de Java 14+ en distintos JDKs.
 */
public class PartidaRecord implements Comparable<PartidaRecord> {
    private final long   semilla;
    private final int    longitudBFS;
    private final int    longitudDFS;
    private final long   tiempoMs;       // Cronómetro: ms para resolver BFS
    private final String metodo;         // "BFS" o "DFS"
    private final int    id;             // Correlativo de partida

    public PartidaRecord(int id, long semilla, int longitudBFS, int longitudDFS, long tiempoMs) {
        this.id          = id;
        this.semilla     = semilla;
        this.longitudBFS = longitudBFS;
        this.longitudDFS = longitudDFS;
        this.tiempoMs    = tiempoMs;
        this.metodo      = "BFS";
    }

    // Getters
    public int    getId()          { return id; }
    public long   getSemilla()     { return semilla; }
    public int    getLongitudBFS() { return longitudBFS; }
    public int    getLongitudDFS() { return longitudDFS; }
    public long   getTiempoMs()    { return tiempoMs; }
    public String getMetodo()      { return metodo; }

    /**
     * Comparación para el árbol: menor tiempo BFS → mayor prioridad en ranking.
     * Como el árbol BST usa clave Integer (id), este Comparable sirve solo
     * para la lógica de top-5 en el árbol.
     */
    @Override
    public int compareTo(PartidaRecord otro) {
        // Primero por longitud BFS (menor = mejor)
        int cmp = Integer.compare(this.longitudBFS, otro.longitudBFS);
        if (cmp != 0) return cmp;
        // Desempate por tiempo
        return Long.compare(this.tiempoMs, otro.tiempoMs);
    }

    @Override
    public String toString() {
        return String.format(
            "Partida#%d | semilla=%-8d | BFS=%3d pasos | DFS=%3d pasos | tiempo=%4d ms",
            id, semilla, longitudBFS, longitudDFS, tiempoMs
        );
    }
}

/**
 * Snapshot inmutable de un laberinto generado con cierta semilla.
 * Guardado en TablaHash&lt;Long, MazeSnapshot&gt; para reutilizar sin regenerar.
 */
public class MazeSnapshot {
    private final long semilla;
    private final int filas;
    private final int columnas;
    // Copia defensiva de la matriz de tipos (char por legibilidad)
    private final char[][] mapa;

    public MazeSnapshot(long semilla, Laberinto lab) {
        this.semilla   = semilla;
        this.filas     = lab.getFilas();
        this.columnas  = lab.getColumnas();
        this.mapa      = new char[filas][columnas];
        for (int i = 0; i < filas; i++)
            for (int j = 0; j < columnas; j++)
                this.mapa[i][j] = lab.getCelda(i, j).getTipo().getSimbolo();
    }

    public long getSemilla()   { return semilla; }
    public int  getFilas()     { return filas;   }
    public int  getColumnas()  { return columnas; }
    public char[][] getMapa()  {
        // Devuelve copia para preservar inmutabilidad
        char[][] copia = new char[filas][columnas];
        for (int i = 0; i < filas; i++)
            copia[i] = mapa[i].clone();
        return copia;
    }

    @Override
    public String toString() {
        return "MazeSnapshot[semilla=" + semilla +
               ", tamaño=" + filas + "×" + columnas + "]";
    }
}

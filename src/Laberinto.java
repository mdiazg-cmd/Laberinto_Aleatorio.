import java.util.Random;

public class Laberinto {
    private static final int PASO_BACKTRACKER = 2;
    private static final int[] D_FILA = {-1, 1, 0, 0};
    private static final int[] D_COL = {0, 0, -1, 1};

    private int filas;
    private int columnas;
    private Celda[][] matriz;
    private Celda celdaInicio;
    private Celda celdaSalida;

    public Laberinto(int filas, int columnas, long semilla) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new Celda[filas][columnas];
        generarLaberinto(semilla);
    }

    public Laberinto(MazeSnapshot snapshot) {
        this.filas = snapshot.getFilas();
        this.columnas = snapshot.getColumnas();
        this.matriz = new Celda[filas][columnas];
        cargarDesdeSnapshot(snapshot);
    }

    private void cargarDesdeSnapshot(MazeSnapshot snapshot) {
        char[][] mapa = snapshot.getMapa();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                TipoCelda tipo = tipoDesdeSimbolo(mapa[i][j]);
                matriz[i][j] = new Celda(i, j, tipo);
                if (tipo == TipoCelda.INICIO) celdaInicio = matriz[i][j];
                if (tipo == TipoCelda.SALIDA) celdaSalida = matriz[i][j];
            }
        }
    }

    private TipoCelda tipoDesdeSimbolo(char simbolo) {
        if (simbolo == TipoCelda.PARED.getSimbolo()) return TipoCelda.PARED;
        if (simbolo == TipoCelda.INICIO.getSimbolo()) return TipoCelda.INICIO;
        if (simbolo == TipoCelda.SALIDA.getSimbolo()) return TipoCelda.SALIDA;
        return TipoCelda.CAMINO;
    }

    private void generarLaberinto(long semilla) {
        Random random = new Random(semilla);

        inicializarParedes();
        ejecutarBacktracker(random);
        conectarSalida();

        // Asegurar Inicio (0,0) y Salida (filas-1, columnas-1)
        matriz[0][0].setTipo(TipoCelda.INICIO);
        this.celdaInicio = matriz[0][0];

        matriz[filas - 1][columnas - 1].setTipo(TipoCelda.SALIDA);
        this.celdaSalida = matriz[filas - 1][columnas - 1];
    }

    private void inicializarParedes() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Celda(i, j, TipoCelda.PARED);
            }
        }
    }

    private void ejecutarBacktracker(Random random) {
        boolean[][] visitadas = new boolean[filas][columnas];
        Pila<Celda> pila = new Pila<>();

        matriz[0][0].setTipo(TipoCelda.CAMINO);
        visitadas[0][0] = true;
        pila.push(matriz[0][0]);

        while (!pila.isEmpty()) {
            Celda actual = pila.peek();
            int direccion = direccionDisponible(actual, visitadas, random);

            if (direccion == -1) {
                pila.pop();
            } else {
                int nf = actual.getFila() + D_FILA[direccion] * PASO_BACKTRACKER;
                int nc = actual.getColumna() + D_COL[direccion] * PASO_BACKTRACKER;
                int paredF = actual.getFila() + D_FILA[direccion];
                int paredC = actual.getColumna() + D_COL[direccion];

                matriz[paredF][paredC].setTipo(TipoCelda.CAMINO);
                matriz[nf][nc].setTipo(TipoCelda.CAMINO);
                visitadas[nf][nc] = true;
                pila.push(matriz[nf][nc]);
            }
        }
    }

    private int direccionDisponible(Celda actual, boolean[][] visitadas, Random random) {
        int[] orden = {0, 1, 2, 3};
        for (int i = 0; i < orden.length; i++) {
            int pos = i + random.nextInt(orden.length - i);
            int temp = orden[i];
            orden[i] = orden[pos];
            orden[pos] = temp;
        }

        for (int i = 0; i < orden.length; i++) {
            int dir = orden[i];
            int nf = actual.getFila() + D_FILA[dir] * PASO_BACKTRACKER;
            int nc = actual.getColumna() + D_COL[dir] * PASO_BACKTRACKER;
            if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas && !visitadas[nf][nc]) {
                return dir;
            }
        }

        return -1;
    }

    private void conectarSalida() {
        int salidaFila = filas - 1;
        int salidaCol = columnas - 1;
        int filaConectada = salidaFila % PASO_BACKTRACKER == 0 ? salidaFila : salidaFila - 1;
        int colConectada = salidaCol % PASO_BACKTRACKER == 0 ? salidaCol : salidaCol - 1;

        if (filaConectada < 0) filaConectada = 0;
        if (colConectada < 0) colConectada = 0;

        for (int i = Math.min(filaConectada, salidaFila); i <= Math.max(filaConectada, salidaFila); i++) {
            matriz[i][colConectada].setTipo(TipoCelda.CAMINO);
        }
        for (int j = Math.min(colConectada, salidaCol); j <= Math.max(colConectada, salidaCol); j++) {
            matriz[salidaFila][j].setTipo(TipoCelda.CAMINO);
        }
    }

    public Celda getCelda(int fila, int col) {
        if (fila >= 0 && fila < filas && col >= 0 && col < columnas) {
            return matriz[fila][col];
        }
        return null; // Fuera de límites
    }

    public Celda getCeldaInicio() { return celdaInicio; }
    public Celda getCeldaSalida() { return celdaSalida; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    // Visualización ASCII
    public void imprimir() {
        System.out.println("=".repeat(columnas * 2 + 2));
        for (int i = 0; i < filas; i++) {
            System.out.print("|");
            for (int j = 0; j < columnas; j++) {
                System.out.print(matriz[i][j].getTipo().getSimbolo() + " ");
            }
            System.out.println("|");
        }
        System.out.println("=".repeat(columnas * 2 + 2));
    }

    public void imprimirConCamino(ListaEnlazadaCamino camino) {
        System.out.println("=".repeat(columnas * 2 + 2));
        for (int i = 0; i < filas; i++) {
            System.out.print("|");
            for (int j = 0; j < columnas; j++) {
                Celda actual = matriz[i][j];
                if (camino != null && camino.contiene(actual)
                        && actual.getTipo() != TipoCelda.INICIO
                        && actual.getTipo() != TipoCelda.SALIDA) {
                    System.out.print("* ");
                } else {
                    System.out.print(actual.getTipo().getSimbolo() + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("=".repeat(columnas * 2 + 2));
    }
    
    // Método para resetear visitadas (útil si queremos resolver varias veces)
    public void resetearVisitadas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j].setVisitada(false);
            }
        }
    }
}

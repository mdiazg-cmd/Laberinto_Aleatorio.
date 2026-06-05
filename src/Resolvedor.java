/**
 * Resolvedor Fase 2.
 * ─ resolverBFS : BFS garantiza camino mínimo (igual que Fase 1 pero con cronómetro).
 * ─ resolverDFS : DFS iterativo usando Pila<Celda> propia; NO garantiza mínimo.
 * ─ comparar    : ejecuta ambos, imprime comparativa de longitudes y tiempo.
 */
public class Resolvedor {

    // ── BFS ───────────────────────────────────────────────────────────────
    /**
     * Resuelve el laberinto con BFS (camino mínimo garantizado).
     * @param laberinto laberinto a resolver
     * @param cronometro cronómetro ya iniciado; se detiene aquí
     * @return camino encontrado, o null si no existe
     */
    public static ListaEnlazadaCamino resolverBFS(Laberinto laberinto, Cronometro cronometro) {
        laberinto.resetearVisitadas();

        Cola<Celda> cola = new Cola<>();
        Celda[][] padre = new Celda[laberinto.getFilas()][laberinto.getColumnas()];

        Celda inicio = laberinto.getCeldaInicio();
        cola.encolar(inicio);
        inicio.setVisitada(true);

        int[] dFila = {-1, 1, 0, 0};
        int[] dCol  = {0, 0, -1, 1};
        boolean encontrado = false;

        while (!cola.isEmpty()) {
            Celda actual = cola.desencolar();
            if (actual == laberinto.getCeldaSalida()) { encontrado = true; break; }

            for (int i = 0; i < 4; i++) {
                int nf = actual.getFila() + dFila[i];
                int nc = actual.getColumna() + dCol[i];
                Celda vecino = laberinto.getCelda(nf, nc);
                if (vecino != null && vecino.getTipo() != TipoCelda.PARED && !vecino.isVisitada()) {
                    vecino.setVisitada(true);
                    padre[nf][nc] = actual;
                    cola.encolar(vecino);
                }
            }
        }

        long tiempoMs = cronometro.detener();

        if (!encontrado) return null;
        return reconstruir(laberinto, padre);
    }

    // ── DFS iterativo con Pila<Celda> propia ──────────────────────────────
    /**
     * Resuelve el laberinto con DFS iterativo usando Pila&lt;Celda&gt; propia.
     * DFS encuentra UN camino pero NO garantiza que sea el mínimo.
     *
     * @param laberinto laberinto a resolver (se resetea visitadas antes)
     * @return camino encontrado, o null si no existe
     */
    public static ListaEnlazadaCamino resolverDFS(Laberinto laberinto) {
        laberinto.resetearVisitadas();

        Pila<Celda> pila  = new Pila<>();
        Celda[][] padre   = new Celda[laberinto.getFilas()][laberinto.getColumnas()];

        Celda inicio = laberinto.getCeldaInicio();
        pila.push(inicio);
        inicio.setVisitada(true);

        int[] dFila = {-1, 1, 0, 0};
        int[] dCol  = {0, 0, -1, 1};
        boolean encontrado = false;

        while (!pila.isEmpty()) {
            Celda actual = pila.pop();

            if (actual == laberinto.getCeldaSalida()) { encontrado = true; break; }

            for (int i = 0; i < 4; i++) {
                int nf = actual.getFila() + dFila[i];
                int nc = actual.getColumna() + dCol[i];
                Celda vecino = laberinto.getCelda(nf, nc);
                if (vecino != null && vecino.getTipo() != TipoCelda.PARED && !vecino.isVisitada()) {
                    vecino.setVisitada(true);
                    padre[nf][nc] = actual;
                    pila.push(vecino);
                }
            }
        }

        if (!encontrado) return null;
        return reconstruir(laberinto, padre);
    }

    // ── Reconstrucción del camino desde la matriz padre ───────────────────
    private static ListaEnlazadaCamino reconstruir(Laberinto lab, Celda[][] padre) {
        Pila<Celda> pilaInv = new Pila<>();
        Celda actual = lab.getCeldaSalida();
        while (actual != null) {
            pilaInv.push(actual);
            actual = padre[actual.getFila()][actual.getColumna()];
        }
        ListaEnlazadaCamino camino = new ListaEnlazadaCamino();
        while (!pilaInv.isEmpty()) camino.agregar(pilaInv.pop());
        return camino;
    }

    // ── Comparación BFS vs DFS ─────────────────────────────────────────────
    /**
     * Ejecuta BFS y DFS sobre el mismo laberinto y muestra la comparativa.
     *
     * @param laberinto laberinto a resolver
     * @return longitud BFS (pasos), útil para el ranking; -1 si sin solución
     */
    public static int[] comparar(Laberinto laberinto) {
        // ── BFS con cronómetro ─────────────────────────────────────
        Cronometro c = new Cronometro();
        c.iniciar();
        ListaEnlazadaCamino caminoBFS = resolverBFS(laberinto, c);
        long tiempoBFS = c.getMs();

        // ── DFS ────────────────────────────────────────────────────
        ListaEnlazadaCamino caminoDFS = resolverDFS(laberinto);

        System.out.println("+-- Comparacion BFS vs DFS ----------------------------+");
        if (caminoBFS == null) {
            System.out.println("|  No existe camino en este laberinto.                 |");
            System.out.println("+------------------------------------------------------+");
            return new int[]{-1, -1};
        }

        int pasosBFS = caminoBFS.getLongitud() - 1;
        int pasosDFS = (caminoDFS != null) ? caminoDFS.getLongitud() - 1 : -1;

        System.out.printf("|  BFS  -> %3d pasos  | tiempo: %4d ms  (MINIMO garantizado)%n", pasosBFS, tiempoBFS);
        if (caminoDFS != null) {
            int diferencia = pasosDFS - pasosBFS;
            System.out.printf("|  DFS  -> %3d pasos  | diferencia: +%d pasos vs BFS%n", pasosDFS, diferencia);
            if (diferencia == 0)
                System.out.println("|  En este laberinto DFS tambien hallo el minimo.     |");
            else
                System.out.printf ("|  DFS tomo %d paso(s) extra (no garantiza minimo).   |%n", diferencia);
        } else {
            System.out.println("|  DFS  -> No encontro camino (BFS si encontro).      |");
        }
        System.out.println("+------------------------------------------------------+");

        System.out.println("Camino minimo encontrado:");
        laberinto.imprimirConCamino(caminoBFS);

        return new int[]{pasosBFS, pasosDFS};
    }
}

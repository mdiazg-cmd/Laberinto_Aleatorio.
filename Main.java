/**
 * Main — Fase 2: Integración completa.
 *
 * Demuestra:
 *  1. TablaHash<Long, MazeSnapshot>   → caché de laberintos por semilla
 *  2. Pila<Celda> + DFS               → comparación de longitud vs BFS
 *  3. ArbolBinario<Integer,PartidaRecord> → ranking; muestra top 5
 *  4. Grafo.desdeMatriz()             → grafo explícito de nodos y aristas
 *  5. Cronometro                      → tiempo de resolución BFS en ms
 */
public class Main {
    private static final int CANTIDAD_LABERINTOS = 5;
    private static final int FILAS_LABERINTO = 10;
    private static final int COLUMNAS_LABERINTO = 10;
    private static final int FACTOR_DESEMPATE_RANKING = 10000;

    public static void main(String[] args) {

        // ────────────────────────────────────────────────────────────────
        // COMPONENTE 1 — TablaHash<Long, MazeSnapshot>
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n+======================================================+");
        System.out.println("|   FASE 2 - LABERINTO ALEATORIO (Integracion Total)  |");
        System.out.println("+======================================================+\n");

        TablaHash<Long, MazeSnapshot> cache = new TablaHash<>();

        // Semillas aleatorias: cada ejecución genera laberintos diferentes.
        long[] semillas = new long[CANTIDAD_LABERINTOS];
        long baseSemilla = System.nanoTime();
        for (int i = 0; i < semillas.length; i++) {
            semillas[i] = baseSemilla + (i * 9973L);
        }

        // ArbolBinario<Integer, PartidaRecord> para ranking
        // Clave = id de partida (correlativo), pero el top-5 lo ordenamos
        // por longitud BFS usando un árbol secundario con clave = longitud.
        ArbolBinario<Integer, PartidaRecord> arbolRanking = new ArbolBinario<>();

        int idPartida = 0;

        // ────────────────────────────────────────────────────────────────
        // BUCLE PRINCIPAL: para cada semilla
        // ────────────────────────────────────────────────────────────────
        for (long semilla : semillas) {

            System.out.println("----------------------------------------------------");
            System.out.println("  Semilla: " + semilla);

            // ── 1a: Revisar caché antes de regenerar ────────────────────
            Laberinto lab;
            if (cache.containsKey(semilla)) {
                System.out.println("  Laberinto recuperado de TablaHash (cache).");
                MazeSnapshot snap = cache.get(semilla);
                lab = new Laberinto(snap);
            } else {
                lab = new Laberinto(FILAS_LABERINTO, COLUMNAS_LABERINTO, semilla);
                MazeSnapshot snap = new MazeSnapshot(semilla, lab);
                cache.put(semilla, snap);
                System.out.println("  Laberinto generado y guardado en TablaHash.");
            }

            lab.imprimir();

            // ── 4: Grafo desde matriz ────────────────────────────────────
            Grafo grafo = Grafo.desdeMatriz(lab);
            grafo.imprimirEstadisticas();

            // ── 2+5: BFS y DFS con cronómetro, comparación ───────────────
            System.out.println();
            int[] pasos = Resolvedor.comparar(lab);
            int pasosBFS = pasos[0];
            int pasosDFS = pasos[1];

            if (pasosBFS == -1) {
                System.out.println("  (Partida sin solución, no se agrega al ranking)\n");
                continue;
            }

            // ── 5: Cronómetro independiente para registrar en PartidaRecord
            Cronometro cron = new Cronometro();
            cron.iniciar();
            Resolvedor.resolverBFS(lab, cron);
            long tiempoMs = cron.getMs();

            // ── 3: Insertar en ArbolBinario ──────────────────────────────
            idPartida++;
            PartidaRecord record = new PartidaRecord(idPartida, semilla, pasosBFS, pasosDFS, tiempoMs);
            arbolRanking.insertar(idPartida, record);
            System.out.printf("  Tiempo BFS registrado: %d ms%n%n", tiempoMs);
        }

        // ────────────────────────────────────────────────────────────────
        // COMPONENTE 3 — Top 5 del ArbolBinario
        // ────────────────────────────────────────────────────────────────
        System.out.println("+======================================================+");
        System.out.println("|             RANKING - TOP 5 PARTIDAS                |");
        System.out.println("+======================================================+");

        // Para ranking real por longitud BFS, insertamos en un segundo árbol
        // con clave = longitudBFS (menor = mejor posición en inorden)
        ArbolBinario<Integer, PartidaRecord> arbolTop = new ArbolBinario<>();
        ListaSimple<PartidaRecord> registros = arbolRanking.inorden();
        for (int i = 0; i < registros.size(); i++) {
            PartidaRecord r = registros.obtener(i);
            // Clave: longitudBFS + id para desempate único
            arbolTop.insertar(r.getLongitudBFS() * FACTOR_DESEMPATE_RANKING + r.getId(), r);
        }

        ListaSimple<PartidaRecord> top5 = arbolTop.top5();
        int pos = 1;
        for (int i = 0; i < top5.size(); i++) {
            PartidaRecord r = top5.obtener(i);
            System.out.printf("|  #%d  %s%n", pos++, r.toString());
        }
        System.out.println("+======================================================+");

        // ────────────────────────────────────────────────────────────────
        // Resumen de la TablaHash
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n  TablaHash: " + cache.size() + " laberinto(s) en caché.");
        System.out.println("\nFase 2 completada.\n");
    }
}

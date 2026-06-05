/**
 * Cronómetro simple de alta resolución.
 * Usa System.nanoTime() internamente; expone resultados en ms.
 *
 * Uso:
 *   Cronometro c = new Cronometro();
 *   c.iniciar();
 *   ... código a medir ...
 *   long ms = c.detener();
 */
public class Cronometro {
    private long inicio;
    private long fin;
    private boolean corriendo;

    public Cronometro() {
        inicio   = 0;
        fin      = 0;
        corriendo = false;
    }

    /** Inicia o reinicia el cronómetro. */
    public void iniciar() {
        inicio   = System.nanoTime();
        fin      = 0;
        corriendo = true;
    }

    /** Detiene el cronómetro y retorna el tiempo transcurrido en ms. */
    public long detener() {
        if (!corriendo) throw new IllegalStateException("Cronómetro no iniciado.");
        fin      = System.nanoTime();
        corriendo = false;
        return getMs();
    }

    /** Tiempo transcurrido en ms (válido tras detener()). */
    public long getMs() {
        long nanosTotal = (corriendo ? System.nanoTime() : fin) - inicio;
        return nanosTotal / 1_000_000L;
    }

    /** Tiempo transcurrido en nanosegundos (más preciso, para benchmarks). */
    public long getNanos() {
        return (corriendo ? System.nanoTime() : fin) - inicio;
    }

    @Override
    public String toString() {
        return getMs() + " ms";
    }
}

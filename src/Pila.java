/**
 * Pila&lt;T&gt; genérica implementada con arreglo dinámico.
 * Operaciones O(1) amortizado: push, pop, peek, isEmpty.
 */
@SuppressWarnings("unchecked")
public class Pila<T> {
    private Object[] datos;
    private int tope;
    private static final int CAPACIDAD_INICIAL = 16;

    public Pila() {
        datos = new Object[CAPACIDAD_INICIAL];
        tope  = -1;
    }

    public void push(T elemento) {
        if (tope + 1 == datos.length) crecer();
        datos[++tope] = elemento;
    }

    public T pop() {
        if (isEmpty()) throw new IllegalStateException("Pila vacia.");
        T val = (T) datos[tope];
        datos[tope--] = null;   // GC-friendly
        return val;
    }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Pila vacia.");
        return (T) datos[tope];
    }

    public boolean isEmpty() { return tope == -1; }
    public int size()        { return tope + 1; }

    private void crecer() {
        Object[] nuevo = new Object[datos.length * 2];
        System.arraycopy(datos, 0, nuevo, 0, datos.length);
        datos = nuevo;
    }

    /** Vacía la pila sin destruir la instancia. */
    public void limpiar() {
        while (!isEmpty()) pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pila[tope→");
        for (int i = tope; i >= 0; i--) {
            sb.append(datos[i]);
            if (i > 0) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}

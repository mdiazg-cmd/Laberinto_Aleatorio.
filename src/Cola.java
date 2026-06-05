/**
 * Cola<T> genérica implementada manualmente con nodos enlazados.
 * Operaciones O(1): encolar, desencolar, frente, isEmpty.
 */
public class Cola<T> {
    private static class NodoCola<T> {
        private T dato;
        private NodoCola<T> siguiente;

        NodoCola(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private NodoCola<T> frente;
    private NodoCola<T> fin;
    private int tamano;

    public Cola() {
        frente = null;
        fin = null;
        tamano = 0;
    }

    public void encolar(T dato) {
        NodoCola<T> nuevo = new NodoCola<>(dato);
        if (isEmpty()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamano++;
    }

    public T desencolar() {
        if (isEmpty()) throw new IllegalStateException("Cola vacia.");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamano--;
        return dato;
    }

    public T frente() {
        if (isEmpty()) throw new IllegalStateException("Cola vacia.");
        return frente.dato;
    }

    public boolean isEmpty() { return tamano == 0; }
    public int size() { return tamano; }
}

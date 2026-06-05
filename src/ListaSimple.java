/**
 * Lista enlazada genérica implementada manualmente.
 * Se usa donde se requiere agrupar elementos sin colecciones de Java.
 */
public class ListaSimple<T> {
    private static class NodoLista<T> {
        private T dato;
        private NodoLista<T> siguiente;

        NodoLista(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private NodoLista<T> cabeza;
    private NodoLista<T> cola;
    private int tamano;

    public ListaSimple() {
        cabeza = null;
        cola = null;
        tamano = 0;
    }

    public void agregar(T dato) {
        NodoLista<T> nuevo = new NodoLista<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            cola = nuevo;
        }
        tamano++;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango.");
        }
        NodoLista<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    public int size() { return tamano; }
    public boolean isEmpty() { return tamano == 0; }
}

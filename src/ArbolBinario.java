/**
 * Árbol Binario de Búsqueda (BST) genérico.
 * Clave K: Comparable → ordena el árbol.
 * Valor V: almacenado en el nodo.
 *
 * Operaciones: insertar, buscar, eliminar, recorridoInorden, top5.
 */
public class ArbolBinario<K extends Comparable<K>, V> {

    // ── Nodo interno ───────────────────────────────────────────────────────
    private static class NodoArbol<K, V> {
        K clave;
        V valor;
        NodoArbol<K, V> izq, der;

        NodoArbol(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    // ── Estado ─────────────────────────────────────────────────────────────
    private NodoArbol<K, V> raiz;
    private int tamano;

    // ── insertar ───────────────────────────────────────────────────────────
    public void insertar(K clave, V valor) {
        raiz = insertarRec(raiz, clave, valor);
    }

    private NodoArbol<K, V> insertarRec(NodoArbol<K, V> nodo, K clave, V valor) {
        if (nodo == null) { tamano++; return new NodoArbol<>(clave, valor); }
        int cmp = clave.compareTo(nodo.clave);
        if      (cmp < 0) nodo.izq = insertarRec(nodo.izq, clave, valor);
        else if (cmp > 0) nodo.der = insertarRec(nodo.der, clave, valor);
        else              nodo.valor = valor;  // actualizar
        return nodo;
    }

    // ── buscar ─────────────────────────────────────────────────────────────
    public V buscar(K clave) {
        NodoArbol<K, V> nodo = raiz;
        while (nodo != null) {
            int cmp = clave.compareTo(nodo.clave);
            if      (cmp < 0) nodo = nodo.izq;
            else if (cmp > 0) nodo = nodo.der;
            else              return nodo.valor;
        }
        return null;
    }

    // ── eliminar ───────────────────────────────────────────────────────────
    public void eliminar(K clave) {
        raiz = eliminarRec(raiz, clave);
    }

    private NodoArbol<K, V> eliminarRec(NodoArbol<K, V> nodo, K clave) {
        if (nodo == null) return null;
        int cmp = clave.compareTo(nodo.clave);
        if (cmp < 0) {
            nodo.izq = eliminarRec(nodo.izq, clave);
        } else if (cmp > 0) {
            nodo.der = eliminarRec(nodo.der, clave);
        } else {
            tamano--;
            if (nodo.izq == null) return nodo.der;
            if (nodo.der == null) return nodo.izq;
            // Sucesor: mínimo del subárbol derecho
            NodoArbol<K, V> sucesor = minimo(nodo.der);
            nodo.clave = sucesor.clave;
            nodo.valor = sucesor.valor;
            nodo.der   = eliminarRec(nodo.der, sucesor.clave);
        }
        return nodo;
    }

    private NodoArbol<K, V> minimo(NodoArbol<K, V> n) {
        while (n.izq != null) n = n.izq;
        return n;
    }

    // ── recorridoInorden → secuencia ordenada por clave ────────────────────
    public ListaSimple<V> inorden() {
        ListaSimple<V> lista = new ListaSimple<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoArbol<K, V> nodo, ListaSimple<V> lista) {
        if (nodo == null) return;
        inordenRec(nodo.izq, lista);
        lista.agregar(nodo.valor);
        inordenRec(nodo.der, lista);
    }

    // ── top5: retorna las 5 primeras entradas del inorden ─────────────────
    public ListaSimple<V> top5() {
        ListaSimple<V> todos = inorden();
        ListaSimple<V> primeros = new ListaSimple<>();
        int limite = Math.min(5, todos.size());
        for (int i = 0; i < limite; i++) {
            primeros.agregar(todos.obtener(i));
        }
        return primeros;
    }

    public int size() { return tamano; }

    // ── mostrarArbol (visualización ASCII compacta) ────────────────────────
    public void imprimirArbol() {
        System.out.println("ArbolBinario (" + tamano + " nodos):");
        imprimirRec(raiz, "", true);
    }

    private void imprimirRec(NodoArbol<K, V> nodo, String prefijo, boolean esUltimo) {
        if (nodo == null) return;
        System.out.println(prefijo + (esUltimo ? "`-- " : "|-- ") + nodo.clave);
        String nuevoPrefijo = prefijo + (esUltimo ? "    " : "|   ");
        imprimirRec(nodo.izq,  nuevoPrefijo, false);
        imprimirRec(nodo.der,  nuevoPrefijo, true);
    }
}

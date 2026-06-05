/**
 * Tabla hash genérica con encadenamiento por separado.
 * Clave K debe implementar equals() y hashCode().
 * Capacidad inicial 16, factor de carga 0.75 → rehash automático.
 */
@SuppressWarnings("unchecked")
public class TablaHash<K, V> {

    // ── Nodo interno de la cadena ──────────────────────────────────────────
    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada<K, V> siguiente;

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    // ── Estado ─────────────────────────────────────────────────────────────
    private Entrada<K, V>[] tabla;
    private int capacidad;
    private int tamano;
    private static final double FACTOR_CARGA = 0.75;

    // ── Constructor ────────────────────────────────────────────────────────
    public TablaHash() {
        this(16);
    }

    public TablaHash(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.tabla = new Entrada[capacidad];
        this.tamano = 0;
    }

    // ── Índice de bucket ───────────────────────────────────────────────────
    private int indice(K clave) {
        int hash = clave.hashCode();
        // Spreads bits and forces positive index
        hash ^= (hash >>> 16);
        return Math.floorMod(hash, capacidad);
    }

    // ── put ────────────────────────────────────────────────────────────────
    public void put(K clave, V valor) {
        // Rehash si superamos el factor de carga
        if ((double) tamano / capacidad >= FACTOR_CARGA) {
            rehash();
        }

        int idx = indice(clave);
        Entrada<K, V> actual = tabla[idx];

        // Si la clave ya existe, actualizamos
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }

        // Insertar al inicio de la cadena (O(1))
        Entrada<K, V> nueva = new Entrada<>(clave, valor);
        nueva.siguiente = tabla[idx];
        tabla[idx] = nueva;
        tamano++;
    }

    // ── get ────────────────────────────────────────────────────────────────
    public V get(K clave) {
        int idx = indice(clave);
        Entrada<K, V> actual = tabla[idx];
        while (actual != null) {
            if (actual.clave.equals(clave)) return actual.valor;
            actual = actual.siguiente;
        }
        return null;
    }

    // ── containsKey ────────────────────────────────────────────────────────
    public boolean containsKey(K clave) {
        int idx = indice(clave);
        Entrada<K, V> actual = tabla[idx];
        while (actual != null) {
            if (actual.clave.equals(clave)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    // ── remove ─────────────────────────────────────────────────────────────
    public boolean remove(K clave) {
        int idx = indice(clave);
        Entrada<K, V> actual = tabla[idx];
        Entrada<K, V> prev = null;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (prev == null) tabla[idx] = actual.siguiente;
                else             prev.siguiente = actual.siguiente;
                tamano--;
                return true;
            }
            prev = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    // ── size ───────────────────────────────────────────────────────────────
    public int size() { return tamano; }

    // ── rehash (duplica capacidad) ─────────────────────────────────────────
    private void rehash() {
        int nuevaCapacidad = capacidad * 2;
        Entrada<K, V>[] nuevaTabla = new Entrada[nuevaCapacidad];

        for (int i = 0; i < capacidad; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                Entrada<K, V> sig = actual.siguiente;
                // Recalcular índice con la nueva capacidad
                int hash = actual.clave.hashCode();
                hash ^= (hash >>> 16);
                int nuevoIdx = Math.floorMod(hash, nuevaCapacidad);
                actual.siguiente = nuevaTabla[nuevoIdx];
                nuevaTabla[nuevoIdx] = actual;
                actual = sig;
            }
        }

        tabla = nuevaTabla;
        capacidad = nuevaCapacidad;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TablaHash{");
        for (int i = 0; i < capacidad; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                sb.append(actual.clave).append("→").append(actual.valor).append(", ");
                actual = actual.siguiente;
            }
        }
        if (sb.length() > 9) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
}

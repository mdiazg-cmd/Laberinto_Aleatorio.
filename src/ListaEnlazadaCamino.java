public class ListaEnlazadaCamino{
    private NodoCamino cabeza;
    private NodoCamino cola;

    public ListaEnlazadaCamino() {
        this.cabeza = null;
        this.cola = null;
    }

    // Insertar al final (para mantener el orden del camino)
    public void agregar(Celda celda) {
        NodoCamino nuevoNodo = new NodoCamino(celda);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            return;
        }
        cola.setSiguiente(nuevoNodo);
        cola = nuevoNodo;
    }

    // Para imprimir el camino fácilmente
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NodoCamino actual = cabeza;
        while (actual != null) {
            sb.append("(").append(actual.getCelda().getFila()).append(",")
            .append(actual.getCelda().getColumna()).append(") ");
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }
    
    public int getLongitud() {
        int count = 0;
        NodoCamino actual = cabeza;
        while (actual != null) {
            count++;
            actual = actual.getSiguiente();
        }
        return count;
    }

    public boolean contiene(Celda celda) {
        NodoCamino actual = cabeza;
        while (actual != null) {
            if (actual.getCelda() == celda) return true;
            actual = actual.getSiguiente();
        }
        return false;
    }
}

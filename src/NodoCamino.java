// Nodo de la lista enlazada
public class NodoCamino {
    private Celda celda;
    private NodoCamino siguiente;

    public NodoCamino(Celda celda) {
        this.celda = celda;
        this.siguiente = null;
    }

    public Celda getCelda() { return celda; }
    public NodoCamino getSiguiente() { return siguiente; }
    public void setSiguiente(NodoCamino siguiente) { this.siguiente = siguiente; }
}

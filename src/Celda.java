public class Celda {
     private int fila;
    private int columna;
    private TipoCelda tipo;
    private boolean visitada; // Para el algoritmo BFS

    public Celda(int fila, int columna, TipoCelda tipo) {
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
        this.visitada = false;
    }

    // Getters y Setters
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public TipoCelda getTipo() { return tipo; }
    public void setTipo(TipoCelda tipo) { this.tipo = tipo; }
    public boolean isVisitada() { return visitada; }
    public void setVisitada(boolean visitada) { this.visitada = visitada; }

    @Override
    public String toString() {
        return String.valueOf(tipo.getSimbolo());
    }
}

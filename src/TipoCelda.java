public enum TipoCelda {
    PARED('#'),
    CAMINO(' '),
    INICIO('S'),
    SALIDA('E');

    private final char simbolo;

    TipoCelda(char simbolo) {
        this.simbolo = simbolo;
    }

    public char getSimbolo() {
        return simbolo;
    }
}

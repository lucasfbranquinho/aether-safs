package br.com.fiap.space.domain.valueobject;

public final class Coordenada {
    private final int eixoX;
    private final int eixoY;

    public Coordenada(int eixoX, int eixoY) {
        if (eixoX < 0 || eixoY < 0) {
            throw new IllegalArgumentException("Coordenadas nao podem ser negativas. Recebido: (" + eixoX + ", " + eixoY + ")");
        }
        this.eixoX = eixoX;
        this.eixoY = eixoY;
    }

    public double distanciaAte(Coordenada destino) {
        int dx = destino.eixoX - this.eixoX;
        int dy = destino.eixoY - this.eixoY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getEixoX() { return eixoX; }
    public int getEixoY() { return eixoY; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordenada that)) return false;
        return eixoX == that.eixoX && eixoY == that.eixoY;
    }

    @Override
    public int hashCode() {
        return 31 * eixoX + eixoY;
    }

    @Override
    public String toString() {
        return "(" + eixoX + ", " + eixoY + ")";
    }
}

package br.com.fiap.space.domain.model;

public enum Terreno {
    PLANICIE("Planicie (seco)", 1.0),
    ALAGADO("Area Alagada", 1.8),
    LAMA("Lama Profunda", 2.5),
    SUBMERSO("Area Submersa", 3.5),
    ESCOMBROS("Escombros", 2.0);

    private final String descricao;
    private final double multiplicadorConsumo;

    Terreno(String descricao, double multiplicadorConsumo) {
        if (multiplicadorConsumo <= 0) throw new IllegalArgumentException("Multiplicador de consumo deve ser positivo.");
        this.descricao = descricao;
        this.multiplicadorConsumo = multiplicadorConsumo;
    }

    public String getDescricao() { return descricao; }
    public double getMultiplicadorConsumo() { return multiplicadorConsumo; }

    @Override
    public String toString() {
        return descricao + " (x" + multiplicadorConsumo + ")";
    }
}

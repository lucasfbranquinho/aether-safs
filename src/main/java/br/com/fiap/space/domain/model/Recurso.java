package br.com.fiap.space.domain.model;

public enum Recurso {
    AGUA("Agua Potavel", 2.0),
    KIT_MEDICO("Kit Medico de Emergencia", 5.0),
    COMIDA("Racao de Sobrevivencia", 3.0),
    RADIO("Radio de Comunicacao", 1.5);

    private final String descricao;
    private final double pesoKg;

    Recurso(String descricao, double pesoKg) {
        if (pesoKg <= 0) throw new IllegalArgumentException("Peso por unidade deve ser positivo.");
        this.descricao = descricao;
        this.pesoKg = pesoKg;
    }

    public String getDescricao() { return descricao; }
    public double getPesoKg() { return pesoKg; }

    @Override
    public String toString() {
        return descricao + " (" + pesoKg + " kg)";
    }
}

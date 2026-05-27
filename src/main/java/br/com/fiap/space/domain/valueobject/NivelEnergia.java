package br.com.fiap.space.domain.valueobject;

import br.com.fiap.space.domain.exception.BateriaCriticaException;

public final class NivelEnergia {
    private final double capacidadeAtual;
    private final double capacidadeMaxima;

    public NivelEnergia(double capacidadeAtual, double capacidadeMaxima) {
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade maxima deve ser positiva.");
        }
        if (capacidadeAtual < 0) {
            throw new IllegalArgumentException("Capacidade atual nao pode ser negativa.");
        }
        if (capacidadeAtual > capacidadeMaxima) {
            throw new IllegalArgumentException("Capacidade atual nao pode exceder a maxima.");
        }
        this.capacidadeAtual = capacidadeAtual;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public NivelEnergia consumir(double quantidade) throws BateriaCriticaException {
        if (quantidade > capacidadeAtual) {
            throw new BateriaCriticaException(String.format(
                "Energia insuficiente! Necessario: %.1f | Disponivel: %.1f", quantidade, capacidadeAtual
            ));
        }
        return new NivelEnergia(capacidadeAtual - quantidade, capacidadeMaxima);
    }

    public NivelEnergia recarregarCompleto() {
        return new NivelEnergia(capacidadeMaxima, capacidadeMaxima);
    }

    public double getCapacidadeAtual() { return capacidadeAtual; }
    public double getCapacidadeMaxima() { return capacidadeMaxima; }
    public double getPercentual() { return (capacidadeAtual / capacidadeMaxima) * 100.0; }

    @Override
    public String toString() {
        return String.format("%.1f/%.1f (%.0f%%)", capacidadeAtual, capacidadeMaxima, getPercentual());
    }
}

package br.com.fiap.space.domain.valueobject;

import br.com.fiap.space.domain.exception.CargaExcedidaException;

public final class CompartimentoKits {
    private final double volumeOcupado;
    private final double volumeMaximo;

    public CompartimentoKits(double volumeOcupado, double volumeMaximo) {
        if (volumeMaximo <= 0) {
            throw new IllegalArgumentException("Volume maximo deve ser positivo.");
        }
        if (volumeOcupado < 0) {
            throw new IllegalArgumentException("Volume ocupado nao pode ser negativo.");
        }
        if (volumeOcupado > volumeMaximo) {
            throw new IllegalArgumentException("Volume ocupado nao pode exceder o maximo.");
        }
        this.volumeOcupado = volumeOcupado;
        this.volumeMaximo = volumeMaximo;
    }

    public CompartimentoKits adicionarKit(double pesoKit) throws CargaExcedidaException {
        if (volumeOcupado + pesoKit > volumeMaximo) {
            throw new CargaExcedidaException(String.format(
                "Carga excedida! Capacidade restante: %.1f kg | Kit solicitado: %.1f kg",
                getCapacidadeRestante(), pesoKit
            ));
        }
        return new CompartimentoKits(volumeOcupado + pesoKit, volumeMaximo);
    }

    public CompartimentoKits esvaziar() {
        return new CompartimentoKits(0, volumeMaximo);
    }

    public boolean estaVazio() { return volumeOcupado == 0.0; }
    public boolean estaCheia() { return volumeOcupado >= volumeMaximo; }
    public double getVolumeOcupado() { return volumeOcupado; }
    public double getVolumeMaximo() { return volumeMaximo; }
    public double getCapacidadeRestante() { return volumeMaximo - volumeOcupado; }

    @Override
    public String toString() {
        return String.format("%.1f/%.1f kg (%.0f%%)", volumeOcupado, volumeMaximo, (volumeOcupado / volumeMaximo) * 100);
    }
}

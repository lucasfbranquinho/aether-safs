package br.com.fiap.space.domain.model;

import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.domain.valueobject.NivelEnergia;

import java.util.ArrayList;
import java.util.List;

public class SondaExploradora extends Sonda {

    private final double alcanceSensor;
    private final List<String> areasMapeadas;
    private int fotosCapturadas;

    public SondaExploradora(String idSonda, NivelEnergia bateria, Coordenada posicaoInicial, double alcanceSensor) {
        super(idSonda, bateria, posicaoInicial);
        if (alcanceSensor <= 0) {
            throw new IllegalArgumentException("Alcance do sensor deve ser estritamente positivo.");
        }
        this.alcanceSensor = alcanceSensor;
        this.areasMapeadas = new ArrayList<>();
        this.fotosCapturadas = 0;
    }

    @Override
    protected void realizarAcaoLocal() {
        System.out.println("  [3/4] Iniciando varredura e mapeamento da area...");
        String area = String.format("Raio %.0fm em torno de %s", alcanceSensor, getPosicaoAtual());
        areasMapeadas.add(area);
        fotosCapturadas += 12;
        System.out.println("  -> Varredura concluida! Alcance do sensor: " + alcanceSensor + "m");
        System.out.println("  -> Imagens capturadas nesta missao: 12 | Total acumulado: " + fotosCapturadas);
        System.out.println("  -> Area registrada: " + area);
        System.out.println("  -> Dados enviados ao AETHER-SAT para analise.");
    }

    @Override
    protected String getRelatorio() {
        return String.format("SondaExploradora | Areas mapeadas: %d | Fotos: %d | Alcance sensor: %.0fm",
                areasMapeadas.size(), fotosCapturadas, alcanceSensor);
    }

    public double getAlcanceSensor() { return alcanceSensor; }
    public List<String> getAreasMapeadas() { return List.copyOf(areasMapeadas); }
    public int getFotosCapturadas() { return fotosCapturadas; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Sensor: %.0fm | Areas: %d | Fotos: %d",
                alcanceSensor, areasMapeadas.size(), fotosCapturadas);
    }
}

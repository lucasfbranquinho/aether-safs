package br.com.fiap.space.domain.model;

import br.com.fiap.space.domain.exception.CargaExcedidaException;
import br.com.fiap.space.domain.exception.TerrenoInvalidoException;
import br.com.fiap.space.domain.valueobject.CompartimentoKits;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.domain.valueobject.NivelEnergia;

import java.util.ArrayList;
import java.util.List;

public class SondaMineradora extends Sonda {

    private CompartimentoKits compartimentoKits;
    private final List<Recurso> kitsCarregados;
    private int entregasRealizadas;

    public SondaMineradora(String idSonda, NivelEnergia bateria, Coordenada posicaoInicial, double capacidadeCarga) {
        super(idSonda, bateria, posicaoInicial);
        this.compartimentoKits = new CompartimentoKits(0, capacidadeCarga);
        this.kitsCarregados = new ArrayList<>();
        this.entregasRealizadas = 0;
    }

    public void carregarKit(Recurso recurso) throws CargaExcedidaException {
        this.compartimentoKits = compartimentoKits.adicionarKit(recurso.getPesoKg());
        kitsCarregados.add(recurso);
        System.out.println("[" + getIdSonda() + "] Kit carregado: " + recurso.getDescricao()
                + " | Carga atual: " + compartimentoKits);
    }

    @Override
    protected void validarTerreno(Terreno terreno) throws TerrenoInvalidoException {
        if (terreno == Terreno.SUBMERSO) {
            throw new TerrenoInvalidoException(
                "Sonda de entrega nao navega em area totalmente submersa! Use um drone aquatico."
            );
        }
    }

    @Override
    protected void realizarAcaoLocal() {
        System.out.println("  [3/4] Realizando entrega de kits de sobrevivencia...");
        if (compartimentoKits.estaVazio()) {
            System.out.println("  -> AVISO: Compartimento vazio. Nenhum kit para entregar.");
            return;
        }
        System.out.println("  -> Entregando " + kitsCarregados.size() + " kit(s) para vitimas:");
        for (Recurso kit : kitsCarregados) {
            System.out.println("     * " + kit.getDescricao() + " (" + kit.getPesoKg() + " kg)");
        }
        entregasRealizadas++;
        kitsCarregados.clear();
        compartimentoKits = compartimentoKits.esvaziar();
        System.out.println("  -> Entrega #" + entregasRealizadas + " concluida com sucesso! Vidas salvas.");
    }

    @Override
    protected String getRelatorio() {
        return String.format("SondaMineradora | Entregas realizadas: %d | Carga: %s",
                entregasRealizadas, compartimentoKits);
    }

    public CompartimentoKits getCompartimentoKits() { return compartimentoKits; }
    public int getEntregasRealizadas() { return entregasRealizadas; }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Carga: %s | Entregas: %d",
                compartimentoKits, entregasRealizadas);
    }
}

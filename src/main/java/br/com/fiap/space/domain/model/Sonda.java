package br.com.fiap.space.domain.model;

import br.com.fiap.space.domain.exception.BateriaCriticaException;
import br.com.fiap.space.domain.exception.TerrenoInvalidoException;
import br.com.fiap.space.domain.interfaces.Recarregavel;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.domain.valueobject.NivelEnergia;

public abstract class Sonda implements Recarregavel {

    private static final double CONSUMO_BASE_POR_UNIDADE = 5.0;

    private final String idSonda;
    private NivelEnergia bateria;
    private Coordenada posicaoAtual;
    private Terreno terrenoAtual;
    private String status;

    protected Sonda(String idSonda, NivelEnergia bateria, Coordenada posicaoInicial) {
        if (idSonda == null || idSonda.isBlank()) {
            throw new IllegalArgumentException("ID da sonda nao pode ser nulo ou vazio.");
        }
        this.idSonda = idSonda;
        this.bateria = bateria;
        this.posicaoAtual = posicaoInicial;
        this.terrenoAtual = Terreno.PLANICIE;
        this.status = "STANDBY";
    }

    // =========================================================
    // TEMPLATE METHOD - esqueleto da rotina autonoma
    // =========================================================
    public final void executarRotinaAutonoma(Coordenada destino, Terreno terreno)
            throws BateriaCriticaException, TerrenoInvalidoException, Exception {

        System.out.println("\n[AETHER-SONDA " + idSonda + "] Iniciando rotina autonoma...");

        // Passo 1: Validar status do sistema
        validarSistema(terreno);

        // Passo 2: Deslocar ate a coordenada
        mover(destino, terreno);

        // Passo 3: Hook - cada sonda realiza sua acao especifica
        realizarAcaoLocal();

        // Passo 4: Enviar relatorio ao Centro de Comando
        enviarRelatorio();

        this.status = "MISSAO_CONCLUIDA";
    }

    private void validarSistema(Terreno terreno) throws BateriaCriticaException, TerrenoInvalidoException {
        System.out.println("  [1/4] Validando sistemas...");
        if (bateria.getPercentual() < 10.0) {
            throw new BateriaCriticaException(String.format(
                "Bateria critica (%.0f%%)! Sonda nao pode iniciar missao. Recarregue antes.", bateria.getPercentual()
            ));
        }
        validarTerreno(terreno);
        System.out.println("  -> Sistemas OK | Bateria: " + bateria);
    }

    protected void validarTerreno(Terreno terreno) throws TerrenoInvalidoException {
        // Hook - subclasses podem sobrescrever para restringir terrenos
    }

    public void mover(Coordenada destino, Terreno terreno) throws BateriaCriticaException {
        System.out.println("  [2/4] Deslocando de " + posicaoAtual + " para " + destino
                + " [" + terreno.getDescricao() + "]...");
        double distancia = posicaoAtual.distanciaAte(destino);
        double consumo = distancia * CONSUMO_BASE_POR_UNIDADE * terreno.getMultiplicadorConsumo();
        this.bateria = bateria.consumir(consumo);
        this.posicaoAtual = destino;
        this.terrenoAtual = terreno;
        System.out.printf("  -> Chegou em %s | Consumo: %.1f | Bateria restante: %s%n",
                posicaoAtual, consumo, bateria);
    }

    protected abstract void realizarAcaoLocal() throws Exception;

    protected abstract String getRelatorio();

    private void enviarRelatorio() {
        System.out.println("  [4/4] Enviando relatorio ao AETHER-COMMAND...");
        System.out.println("  -> " + getRelatorio());
    }

    @Override
    public void conectarBase() {
        System.out.println("[SONDA " + idSonda + "] Conectando a base AETHER para recarga...");
        this.bateria = bateria.recarregarCompleto();
        this.status = "STANDBY";
        System.out.println("  -> Recarga completa! Bateria: " + bateria);
    }

    public String getIdSonda() { return idSonda; }
    public NivelEnergia getBateria() { return bateria; }
    public Coordenada getPosicaoAtual() { return posicaoAtual; }
    public Terreno getTerrenoAtual() { return terrenoAtual; }
    public String getStatus() { return status; }

    protected void setStatus(String status) { this.status = status; }
    protected void setBateria(NivelEnergia bateria) { this.bateria = bateria; }

    @Override
    public String toString() {
        return String.format("[%s] Pos: %s | Bateria: %s | Status: %s",
                idSonda, posicaoAtual, bateria, status);
    }
}

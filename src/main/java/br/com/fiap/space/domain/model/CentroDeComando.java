package br.com.fiap.space.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// SINGLETON - so pode existir um Centro de Comando gerenciando toda a frota
public class CentroDeComando {

    private static CentroDeComando instancia;

    private final List<Sonda> frota;
    private int totalMissoesExecutadas;

    private CentroDeComando() {
        this.frota = new ArrayList<>();
        this.totalMissoesExecutadas = 0;
        System.out.println("=== AETHER-COMMAND ONLINE ===");
        System.out.println("Centro de Comando inicializado. Aguardando sondas...\n");
    }

    public static CentroDeComando getInstance() {
        if (instancia == null) {
            instancia = new CentroDeComando();
        }
        return instancia;
    }

    public void registrarSonda(Sonda sonda) {
        frota.add(sonda);
        System.out.println("[COMANDO] Sonda registrada na frota: " + sonda.getIdSonda());
    }

    public Optional<Sonda> buscarSonda(String id) {
        return frota.stream()
                .filter(s -> s.getIdSonda().equalsIgnoreCase(id))
                .findFirst();
    }

    public void registrarMissaoConcluida() {
        totalMissoesExecutadas++;
    }

    public List<Sonda> getFrota() { return List.copyOf(frota); }
    public int getTotalMissoesExecutadas() { return totalMissoesExecutadas; }

    public void exibirStatusFrota() {
        System.out.println("\n+----------------------------------------------------------+");
        System.out.println("|          STATUS DA FROTA AETHER 2.0                     |");
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("|  Sondas ativas: %-5d  |  Missoes concluidas: %-10d|%n",
                frota.size(), totalMissoesExecutadas);
        System.out.println("+----------------------------------------------------------+");
        if (frota.isEmpty()) {
            System.out.println("|  Nenhuma sonda cadastrada ainda.                        |");
        } else {
            for (Sonda s : frota) {
                System.out.println("  " + s);
            }
        }
        System.out.println("+----------------------------------------------------------+");
    }
}

package br.com.fiap.space.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// SINGLETON - so pode existir um Centro de Comando gerenciando toda a frota
public class CentroDeComandoAETHER {

    private static CentroDeComandoAETHER instancia;

    private final List<Rover> frota;
    private int totalMissoesExecutadas;

    private CentroDeComandoAETHER() {
        this.frota = new ArrayList<>();
        this.totalMissoesExecutadas = 0;
        System.out.println("=== AETHER-COMMAND ONLINE ===");
        System.out.println("Centro de Comando inicializado. Aguardando rovers...\n");
    }

    public static CentroDeComandoAETHER getInstance() {
        if (instancia == null) {
            instancia = new CentroDeComandoAETHER();
        }
        return instancia;
    }

    public void registrarRover(Rover rover) {
        frota.add(rover);
        System.out.println("[COMANDO] Rover registrado na frota: " + rover.getIdRover());
    }

    public Optional<Rover> buscarRover(String id) {
        return frota.stream()
                .filter(r -> r.getIdRover().equalsIgnoreCase(id))
                .findFirst();
    }

    public void registrarMissaoConcluida() {
        totalMissoesExecutadas++;
    }

    public List<Rover> getFrota() { return List.copyOf(frota); }
    public int getTotalMissoesExecutadas() { return totalMissoesExecutadas; }

    public void exibirStatusFrota() {
        System.out.println("\n+----------------------------------------------------------+");
        System.out.println("|          STATUS DA FROTA AETHER 2.0                     |");
        System.out.println("+----------------------------------------------------------+");
        System.out.printf("|  Rovers ativos: %-5d  |  Missoes concluidas: %-10d|%n",
                frota.size(), totalMissoesExecutadas);
        System.out.println("+----------------------------------------------------------+");
        if (frota.isEmpty()) {
            System.out.println("|  Nenhum rover cadastrado ainda.                         |");
        } else {
            for (Rover r : frota) {
                System.out.println("  " + r);
            }
        }
        System.out.println("+----------------------------------------------------------+");
    }
}

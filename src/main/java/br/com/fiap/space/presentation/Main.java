package br.com.fiap.space.presentation;

import br.com.fiap.space.application.MissaoService;
import br.com.fiap.space.domain.model.Recurso;
import br.com.fiap.space.domain.model.Rover;
import br.com.fiap.space.domain.model.Terreno;
import br.com.fiap.space.infrastructure.RoverRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final MissaoService missaoService = new MissaoService(new RoverRepositoryImpl());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirCabecalho();
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            switch (opcao) {
                case 1 -> menuLancarRover();
                case 2 -> missaoService.exibirStatusFrota();
                case 3 -> menuExecutarMissao();
                case 4 -> menuCarregarKit();
                case 5 -> menuRecarregarRover();
                case 6 -> listarRovers();
                case 0 -> System.out.println("\n[AETHER-COMMAND] Sistema encerrado. Misso cumprida.\n");
                default -> System.out.println("[ERRO] Opcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // ===== MENUS =====

    private static void menuLancarRover() {
        System.out.println("\n--- LANCAR NOVO ROVER ---");
        System.out.println("[1] RoverEntregaKit  - entrega kits de sobrevivencia em areas alagadas");
        System.out.println("[2] RoverMapeamento  - mapeia areas atingidas com sensor de longo alcance");
        int escolha = lerInteiro("Tipo: ");
        String tipo = switch (escolha) {
            case 1 -> "ENTREGA";
            case 2 -> "MAPEAMENTO";
            default -> null;
        };
        if (tipo == null) {
            System.out.println("[ERRO] Tipo invalido.");
            return;
        }
        try {
            Rover rover = missaoService.lancarRover(tipo);
            System.out.println("[OK] Rover lancado: " + rover.getIdRover());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private static void menuExecutarMissao() {
        System.out.println("\n--- EXECUTAR MISSAO DE RESGATE ---");
        listarRovers();
        if (missaoService.listarRovers().isEmpty()) return;

        String id = lerString("ID do rover (ex: RVR-ENT-001): ").toUpperCase();
        int x = lerInteiro("Coordenada X do destino: ");
        int y = lerInteiro("Coordenada Y do destino: ");

        System.out.println("\nTipos de terreno disponiveis:");
        for (Terreno t : Terreno.values()) {
            System.out.println("  " + t.name() + " -> " + t);
        }
        String terreno = lerString("Tipo de terreno: ");

        try {
            missaoService.executarMissao(id, x, y, terreno);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[!] ALERTA SISTEMA: " + e.getMessage());
        }
    }

    private static void menuCarregarKit() {
        System.out.println("\n--- CARREGAR KIT EM ROVER DE ENTREGA ---");
        listarRovers();
        if (missaoService.listarRovers().isEmpty()) return;

        String id = lerString("ID do RoverEntregaKit (ex: RVR-ENT-001): ").toUpperCase();

        System.out.println("\nRecursos disponiveis:");
        for (Recurso r : Recurso.values()) {
            System.out.println("  " + r.name() + " -> " + r);
        }
        String recurso = lerString("Tipo de recurso: ");

        try {
            missaoService.carregarKit(id, recurso);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[!] ALERTA: " + e.getMessage());
        }
    }

    private static void menuRecarregarRover() {
        System.out.println("\n--- RECARREGAR BATERIA DO ROVER ---");
        listarRovers();
        if (missaoService.listarRovers().isEmpty()) return;

        String id = lerString("ID do rover: ").toUpperCase();
        try {
            missaoService.recarregarRover(id);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private static void listarRovers() {
        List<Rover> rovers = missaoService.listarRovers();
        if (rovers.isEmpty()) {
            System.out.println("[INFO] Nenhum rover cadastrado. Use a opcao [1] para lancar um rover.");
            return;
        }
        System.out.println("\n--- ROVERS CADASTRADOS (" + rovers.size() + ") ---");
        rovers.forEach(r -> System.out.println("  " + r));
    }

    // ===== UTILITARIOS =====

    private static void exibirCabecalho() {
        System.out.println();
        System.out.println("  ___   _____ _____ _   _ ___________    ___   ___  ");
        System.out.println(" / _ \\ |  ___|_   _| | | |  ___| ___ \\  |_  | |   | ");
        System.out.println("/ /_\\ \\| |__   | | | |_| | |__ | |_/ /    | | | . | ");
        System.out.println("|  _  ||  __|  | | |  _  |  __||    /     | | |   / ");
        System.out.println("| | | || |___  | | | | | | |___| |\\ \\  /\\__/ / |  /  ");
        System.out.println("\\_| |_/\\____/  \\_/ \\_| |_|\\____/\\_| \\_| \\____/  \\_/  ");
        System.out.println();
        System.out.println("  AETHER 2.0 - Reverse Space Engineering");
        System.out.println("  Surface Autonomous Fleet System (SAFS)");
        System.out.println("  FIAP Global Solution 2026 | Prof. Eduardo Ramos");
        System.out.println();
        System.out.println("  \"A NASA vai gastar US$ 100 bilhoes pra ir a Marte.");
        System.out.println("   Em maio de 2024, 478 cidades brasileiras viraram Marte.\"");
        System.out.println();
    }

    private static void exibirMenu() {
        System.out.println("\n========================================");
        System.out.println("   AETHER-COMMAND - Menu Principal");
        System.out.println("========================================");
        System.out.println(" [1] Lancar novo Rover (Factory)");
        System.out.println(" [2] Status da frota (AETHER-COMMAND)");
        System.out.println(" [3] Executar missao de resgate");
        System.out.println(" [4] Carregar kit em RoverEntregaKit");
        System.out.println(" [5] Recarregar bateria de rover");
        System.out.println(" [6] Listar todos os rovers");
        System.out.println(" [0] Encerrar sistema");
        System.out.println("========================================");
    }

    private static int lerInteiro(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Digite um numero inteiro valido.");
            }
        }
    }

    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}

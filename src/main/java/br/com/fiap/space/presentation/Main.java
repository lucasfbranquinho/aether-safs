package br.com.fiap.space.presentation;

import br.com.fiap.space.application.MissaoService;
import br.com.fiap.space.domain.model.Recurso;
import br.com.fiap.space.domain.model.Sonda;
import br.com.fiap.space.domain.model.Terreno;
import br.com.fiap.space.infrastructure.SondaRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final MissaoService missaoService = new MissaoService(new SondaRepositoryImpl());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirCabecalho();
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            switch (opcao) {
                case 1 -> menuLancarSonda();
                case 2 -> missaoService.exibirStatusFrota();
                case 3 -> menuExecutarMissao();
                case 4 -> menuCarregarKit();
                case 5 -> menuRecarregarSonda();
                case 6 -> listarSondas();
                case 0 -> System.out.println("\n[AETHER-COMMAND] Sistema encerrado. Missao cumprida.\n");
                default -> System.out.println("[ERRO] Opcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // ===== MENUS =====

    private static void menuLancarSonda() {
        System.out.println("\n--- LANCAR NOVA SONDA ---");
        System.out.println("[1] SondaMineradora  - entrega kits de sobrevivencia em areas alagadas");
        System.out.println("[2] SondaExploradora - mapeia areas atingidas com sensor de longo alcance");
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
            Sonda sonda = missaoService.lancarSonda(tipo);
            System.out.println("[OK] Sonda lancada: " + sonda.getIdSonda());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private static void menuExecutarMissao() {
        System.out.println("\n--- EXECUTAR MISSAO DE RESGATE ---");
        listarSondas();
        if (missaoService.listarSondas().isEmpty()) return;

        String id = lerString("ID da sonda (ex: SND-MIN-001): ").toUpperCase();
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
        System.out.println("\n--- CARREGAR KIT EM SONDA MINERADORA ---");
        listarSondas();
        if (missaoService.listarSondas().isEmpty()) return;

        String id = lerString("ID da SondaMineradora (ex: SND-MIN-001): ").toUpperCase();

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

    private static void menuRecarregarSonda() {
        System.out.println("\n--- RECARREGAR BATERIA DA SONDA ---");
        listarSondas();
        if (missaoService.listarSondas().isEmpty()) return;

        String id = lerString("ID da sonda: ").toUpperCase();
        try {
            missaoService.recarregarSonda(id);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private static void listarSondas() {
        List<Sonda> sondas = missaoService.listarSondas();
        if (sondas.isEmpty()) {
            System.out.println("[INFO] Nenhuma sonda cadastrada. Use a opcao [1] para lancar uma sonda.");
            return;
        }
        System.out.println("\n--- SONDAS CADASTRADAS (" + sondas.size() + ") ---");
        sondas.forEach(s -> System.out.println("  " + s));
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
        System.out.println(" [1] Lancar nova Sonda (Factory)");
        System.out.println(" [2] Status da frota (AETHER-COMMAND)");
        System.out.println(" [3] Executar missao de resgate");
        System.out.println(" [4] Carregar kit em SondaMineradora");
        System.out.println(" [5] Recarregar bateria de sonda");
        System.out.println(" [6] Listar todas as sondas");
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

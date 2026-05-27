package br.com.fiap.space.infrastructure.factory;

import br.com.fiap.space.domain.model.Rover;
import br.com.fiap.space.domain.model.RoverEntregaKit;
import br.com.fiap.space.domain.model.RoverMapeamento;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.domain.valueobject.NivelEnergia;

import java.util.concurrent.atomic.AtomicInteger;

// FACTORY METHOD - decide qual subclasse instanciar com base no tipo
public class RoverFactory {

    private static final AtomicInteger contadorEntrega = new AtomicInteger(1);
    private static final AtomicInteger contadorMapeamento = new AtomicInteger(1);

    public static Rover criar(String tipo) {
        return switch (tipo.toUpperCase().trim()) {
            case "ENTREGA" -> criarRoverEntrega();
            case "MAPEAMENTO" -> criarRoverMapeamento();
            default -> throw new IllegalArgumentException(
                "Tipo de rover invalido: '" + tipo + "'. Tipos validos: ENTREGA | MAPEAMENTO"
            );
        };
    }

    private static RoverEntregaKit criarRoverEntrega() {
        String id = String.format("RVR-ENT-%03d", contadorEntrega.getAndIncrement());
        return new RoverEntregaKit(
                id,
                new NivelEnergia(100.0, 100.0),
                new Coordenada(0, 0),
                50.0
        );
    }

    private static RoverMapeamento criarRoverMapeamento() {
        String id = String.format("RVR-MAP-%03d", contadorMapeamento.getAndIncrement());
        return new RoverMapeamento(
                id,
                new NivelEnergia(100.0, 100.0),
                new Coordenada(0, 0),
                500.0
        );
    }
}

package br.com.fiap.space.infrastructure.factory;

import br.com.fiap.space.domain.model.Sonda;
import br.com.fiap.space.domain.model.SondaMineradora;
import br.com.fiap.space.domain.model.SondaExploradora;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.domain.valueobject.NivelEnergia;

import java.util.concurrent.atomic.AtomicInteger;

// FACTORY METHOD - decide qual subclasse instanciar com base no tipo
public class SondaFactory {

    private static final AtomicInteger contadorMineradora = new AtomicInteger(1);
    private static final AtomicInteger contadorExploradora = new AtomicInteger(1);

    public static Sonda criar(String tipo) {
        return switch (tipo.toUpperCase().trim()) {
            case "ENTREGA" -> criarSondaMineradora();
            case "MAPEAMENTO" -> criarSondaExploradora();
            default -> throw new IllegalArgumentException(
                "Tipo de sonda invalido: '" + tipo + "'. Tipos validos: ENTREGA | MAPEAMENTO"
            );
        };
    }

    private static SondaMineradora criarSondaMineradora() {
        String id = String.format("SND-MIN-%03d", contadorMineradora.getAndIncrement());
        return new SondaMineradora(
                id,
                new NivelEnergia(100.0, 100.0),
                new Coordenada(0, 0),
                50.0
        );
    }

    private static SondaExploradora criarSondaExploradora() {
        String id = String.format("SND-EXP-%03d", contadorExploradora.getAndIncrement());
        return new SondaExploradora(
                id,
                new NivelEnergia(100.0, 100.0),
                new Coordenada(0, 0),
                500.0
        );
    }
}

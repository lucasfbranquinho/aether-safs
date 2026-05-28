package br.com.fiap.space.application;

import br.com.fiap.space.domain.exception.BateriaCriticaException;
import br.com.fiap.space.domain.exception.CargaExcedidaException;
import br.com.fiap.space.domain.exception.TerrenoInvalidoException;
import br.com.fiap.space.domain.interfaces.SondaRepository;
import br.com.fiap.space.domain.model.*;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.infrastructure.factory.SondaFactory;

import java.util.List;
import java.util.Optional;

public class MissaoService {

    private final SondaRepository sondaRepository;
    private final CentroDeComando centroDeComando;

    public MissaoService(SondaRepository sondaRepository) {
        this.sondaRepository = sondaRepository;
        this.centroDeComando = CentroDeComando.getInstance();
    }

    public Sonda lancarSonda(String tipo) {
        Sonda sonda = SondaFactory.criar(tipo);
        sondaRepository.salvar(sonda);
        centroDeComando.registrarSonda(sonda);
        return sonda;
    }

    public List<Sonda> listarSondas() {
        return sondaRepository.listarTodos();
    }

    public void executarMissao(String idSonda, int x, int y, String tipoTerreno)
            throws BateriaCriticaException, TerrenoInvalidoException, Exception {

        Sonda sonda = buscarOuLancarErro(idSonda);
        Coordenada destino = new Coordenada(x, y);
        Terreno terreno;
        try {
            terreno = Terreno.valueOf(tipoTerreno.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Terreno invalido: '" + tipoTerreno + "'.");
        }

        sonda.executarRotinaAutonoma(destino, terreno);
        centroDeComando.registrarMissaoConcluida();
        sondaRepository.atualizar(sonda);
    }

    public void carregarKit(String idSonda, String tipoRecurso) throws CargaExcedidaException {
        Sonda sonda = buscarOuLancarErro(idSonda);
        if (!(sonda instanceof SondaMineradora sondaMineradora)) {
            throw new IllegalStateException("Apenas SondaMineradora pode carregar kits! Sonda selecionada e do tipo: "
                    + sonda.getClass().getSimpleName());
        }
        Recurso recurso;
        try {
            recurso = Recurso.valueOf(tipoRecurso.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Recurso invalido: '" + tipoRecurso + "'.");
        }
        sondaMineradora.carregarKit(recurso);
        sondaRepository.atualizar(sonda);
    }

    public void recarregarSonda(String idSonda) {
        Sonda sonda = buscarOuLancarErro(idSonda);
        sonda.conectarBase();
        sondaRepository.atualizar(sonda);
    }

    public void exibirStatusFrota() {
        centroDeComando.exibirStatusFrota();
    }

    private Sonda buscarOuLancarErro(String idSonda) {
        Optional<Sonda> opt = sondaRepository.buscarPorId(idSonda);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Sonda nao encontrada: '" + idSonda + "'.");
        }
        return opt.get();
    }
}

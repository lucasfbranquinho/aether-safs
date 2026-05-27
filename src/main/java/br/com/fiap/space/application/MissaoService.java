package br.com.fiap.space.application;

import br.com.fiap.space.domain.exception.BateriaCriticaException;
import br.com.fiap.space.domain.exception.CargaExcedidaException;
import br.com.fiap.space.domain.exception.TerrenoInvalidoException;
import br.com.fiap.space.domain.interfaces.RoverRepository;
import br.com.fiap.space.domain.model.*;
import br.com.fiap.space.domain.valueobject.Coordenada;
import br.com.fiap.space.infrastructure.factory.RoverFactory;

import java.util.List;
import java.util.Optional;

public class MissaoService {

    private final RoverRepository roverRepository;
    private final CentroDeComandoAETHER centroDeComando;

    public MissaoService(RoverRepository roverRepository) {
        this.roverRepository = roverRepository;
        this.centroDeComando = CentroDeComandoAETHER.getInstance();
    }

    public Rover lancarRover(String tipo) {
        Rover rover = RoverFactory.criar(tipo);
        roverRepository.salvar(rover);
        centroDeComando.registrarRover(rover);
        return rover;
    }

    public List<Rover> listarRovers() {
        return roverRepository.listarTodos();
    }

    public void executarMissao(String idRover, int x, int y, String tipoTerreno)
            throws BateriaCriticaException, TerrenoInvalidoException, Exception {

        Rover rover = buscarOuLancarErro(idRover);
        Coordenada destino = new Coordenada(x, y);
        Terreno terreno;
        try {
            terreno = Terreno.valueOf(tipoTerreno.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Terreno invalido: '" + tipoTerreno + "'.");
        }

        rover.executarRotinaAutonoma(destino, terreno);
        centroDeComando.registrarMissaoConcluida();
        roverRepository.atualizar(rover);
    }

    public void carregarKit(String idRover, String tipoRecurso) throws CargaExcedidaException {
        Rover rover = buscarOuLancarErro(idRover);
        if (!(rover instanceof RoverEntregaKit roverEntrega)) {
            throw new IllegalStateException("Apenas RoverEntregaKit pode carregar kits! Rover selecionado e do tipo: "
                    + rover.getClass().getSimpleName());
        }
        Recurso recurso;
        try {
            recurso = Recurso.valueOf(tipoRecurso.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Recurso invalido: '" + tipoRecurso + "'.");
        }
        roverEntrega.carregarKit(recurso);
        roverRepository.atualizar(rover);
    }

    public void recarregarRover(String idRover) {
        Rover rover = buscarOuLancarErro(idRover);
        rover.conectarBase();
        roverRepository.atualizar(rover);
    }

    public void exibirStatusFrota() {
        centroDeComando.exibirStatusFrota();
    }

    private Rover buscarOuLancarErro(String idRover) {
        Optional<Rover> opt = roverRepository.buscarPorId(idRover);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Rover nao encontrado: '" + idRover + "'.");
        }
        return opt.get();
    }
}

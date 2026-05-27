package br.com.fiap.space.domain.interfaces;

import br.com.fiap.space.domain.model.Rover;

import java.util.List;
import java.util.Optional;

public interface RoverRepository {
    void salvar(Rover rover);
    Optional<Rover> buscarPorId(String id);
    List<Rover> listarTodos();
    void atualizar(Rover rover);
}

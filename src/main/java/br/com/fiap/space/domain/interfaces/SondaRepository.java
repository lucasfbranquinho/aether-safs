package br.com.fiap.space.domain.interfaces;

import br.com.fiap.space.domain.model.Sonda;

import java.util.List;
import java.util.Optional;

public interface SondaRepository {
    void salvar(Sonda sonda);
    Optional<Sonda> buscarPorId(String id);
    List<Sonda> listarTodos();
    void atualizar(Sonda sonda);
}

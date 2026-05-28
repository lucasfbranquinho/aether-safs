package br.com.fiap.space.infrastructure;

import br.com.fiap.space.domain.interfaces.SondaRepository;
import br.com.fiap.space.domain.model.Sonda;

import java.util.*;

public class SondaRepositoryImpl implements SondaRepository {

    private final Map<String, Sonda> storage = new LinkedHashMap<>();

    @Override
    public void salvar(Sonda sonda) {
        storage.put(sonda.getIdSonda(), sonda);
    }

    @Override
    public Optional<Sonda> buscarPorId(String id) {
        return Optional.ofNullable(storage.get(id.toUpperCase()));
    }

    @Override
    public List<Sonda> listarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void atualizar(Sonda sonda) {
        storage.put(sonda.getIdSonda(), sonda);
    }
}

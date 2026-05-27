package br.com.fiap.space.infrastructure;

import br.com.fiap.space.domain.interfaces.RoverRepository;
import br.com.fiap.space.domain.model.Rover;

import java.util.*;

public class RoverRepositoryImpl implements RoverRepository {

    private final Map<String, Rover> storage = new LinkedHashMap<>();

    @Override
    public void salvar(Rover rover) {
        storage.put(rover.getIdRover(), rover);
    }

    @Override
    public Optional<Rover> buscarPorId(String id) {
        return Optional.ofNullable(storage.get(id.toUpperCase()));
    }

    @Override
    public List<Rover> listarTodos() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void atualizar(Rover rover) {
        storage.put(rover.getIdRover(), rover);
    }
}

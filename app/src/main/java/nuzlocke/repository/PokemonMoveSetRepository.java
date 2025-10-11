package nuzlocke.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.PokemonMoveSet;

public interface PokemonMoveSetRepository extends CrudRepository<PokemonMoveSet, Long> {
    List<PokemonMoveSet> findByPokemon_PokemonName(String pokemonName);
}

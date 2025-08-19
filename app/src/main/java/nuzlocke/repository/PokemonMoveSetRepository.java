package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.PokemonMoveSet;

public interface PokemonMoveSetRepository extends CrudRepository<PokemonMoveSet, Long> {

}

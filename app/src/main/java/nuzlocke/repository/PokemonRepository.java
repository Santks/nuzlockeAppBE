package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;
import nuzlocke.domain.Pokemon;

public interface PokemonRepository extends CrudRepository<Pokemon, Long> {

}

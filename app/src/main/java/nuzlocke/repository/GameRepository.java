package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.Game;

public interface GameRepository extends CrudRepository<Game, Long> {

}

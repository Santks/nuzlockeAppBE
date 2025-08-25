package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.TrainerTeam;

public interface TrainerTeamRepository extends CrudRepository<TrainerTeam, Long> {

}

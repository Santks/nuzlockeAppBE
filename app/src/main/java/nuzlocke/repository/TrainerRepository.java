package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.Trainer;

public interface TrainerRepository extends CrudRepository<Trainer, Long> {

}

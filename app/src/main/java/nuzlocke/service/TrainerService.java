package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Trainer;
import nuzlocke.repository.TrainerRepository;

@Service
public class TrainerService {

    // TODO: FETCH TEAMS BY TRAINER??

    private final TrainerRepository trainerRepo;

    @Autowired
    public TrainerService(TrainerRepository trainerRepo) {
        this.trainerRepo = trainerRepo;
    }

    public Iterable<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }

    public Trainer getTrainerById(@PathVariable Long trainerId) {
        return trainerRepo.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("No trainer found with id: " + trainerId));
    }

    public Trainer createNewTrainer(@RequestBody Trainer newTrainer) {
        Trainer existingTrainer = trainerRepo.findByTrainerName(newTrainer.getTrainerName());
        if (existingTrainer != null && existingTrainer.getTrainerName().equals(newTrainer.getTrainerName())
                && existingTrainer.getRoute().equals(newTrainer.getRoute())) {
            throw new IllegalArgumentException("Trainer with name: " + existingTrainer.getTrainerName()
                    + " already exists on this route: " + existingTrainer.getRoute().getRouteName());
        }
        return trainerRepo.save(newTrainer);
    }

    @Transactional
    public Trainer editTrainer(@PathVariable Long trainerId, @RequestBody Trainer editedTrainer) {
        return trainerRepo.findById(trainerId).map(existingTrainer -> {
            existingTrainer.setRoute(editedTrainer.getRoute());
            existingTrainer.setTrainerName(editedTrainer.getTrainerName());
            existingTrainer.setTrainerTeams(editedTrainer.getTrainerTeams());

            return trainerRepo.save(existingTrainer);
        })
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with id" + trainerId));
    }

    public void deleteTrainerById(@PathVariable Long trainerId) {
        if (!trainerRepo.existsById(trainerId)) {
            throw new EntityNotFoundException("Trainer not found with id: " + trainerId);
        }
        trainerRepo.deleteById(trainerId);
    }

}

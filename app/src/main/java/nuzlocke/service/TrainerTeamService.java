package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.TrainerTeam;
import nuzlocke.repository.TrainerTeamRepository;

@Service
public class TrainerTeamService {

    private final TrainerTeamRepository ttRepo;

    @Autowired
    public TrainerTeamService(TrainerTeamRepository ttRepo) {
        this.ttRepo = ttRepo;
    }

    public Iterable<TrainerTeam> getAllTrainerTeams() {
        return ttRepo.findAll();
    }

    public TrainerTeam getTrainerTeamById(@PathVariable Long trainerTeamId) {
        return ttRepo.findById(trainerTeamId)
                .orElseThrow(() -> new EntityNotFoundException("No trainer team found with id: " + trainerTeamId));
    }

    public TrainerTeam createNewTrainerTeam(@RequestBody TrainerTeam newTrainerTeam) {
        if (newTrainerTeam == null) {
            throw new IllegalArgumentException("Trainer team cannot be empty or null");
        }
        return ttRepo.save(newTrainerTeam);
    }

    @Transactional
    public TrainerTeam editTrainerTeam(@PathVariable Long trainerTeamId, @RequestBody TrainerTeam editedTrainerTeam) {
        return ttRepo.findById(trainerTeamId).map(existingTeam -> {
            existingTeam.setTrainer(editedTrainerTeam.getTrainer());
            existingTeam.setPokemons(editedTrainerTeam.getPokemons());

            return ttRepo.save(existingTeam);
        })
                .orElseThrow(() -> new EntityNotFoundException("No trainer team found with id: " + trainerTeamId));
    }

    public void deleteTrainerTeamById(@PathVariable Long trainerTeamId) {
        if (!ttRepo.existsById(trainerTeamId)) {
            throw new EntityNotFoundException("No trainer team found with id: " + trainerTeamId);
        }
        ttRepo.deleteById(trainerTeamId);
    }
}

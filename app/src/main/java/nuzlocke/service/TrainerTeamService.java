package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.domain.Trainer;
import nuzlocke.domain.TrainerTeam;
import nuzlocke.repository.TrainerTeamRepository;

@Service
public class TrainerTeamService {

    private final TrainerTeamRepository ttRepo;
    private final IdempotencyRecordService idempotencyService;
    private final TrainerService trainerService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TrainerTeamService(TrainerTeamRepository ttRepo, IdempotencyRecordService idempotencyService,
            TrainerService trainerService,
            ObjectMapper objectMapper) {
        this.ttRepo = ttRepo;
        this.idempotencyService = idempotencyService;
        this.trainerService = trainerService;
        this.objectMapper = objectMapper;
    }

    public Iterable<TrainerTeam> getAllTrainerTeams() {
        return ttRepo.findAll();
    }

    public TrainerTeam getTrainerTeamById(Long trainerTeamId) {
        return ttRepo.findById(trainerTeamId)
                .orElseThrow(() -> new EntityNotFoundException("No trainer team found with id: " + trainerTeamId));
    }

    @Transactional
    public TrainerTeam createNewTrainerTeam(String key, TrainerTeam newTrainerTeam)
            throws JsonMappingException, JsonProcessingException {
        if (newTrainerTeam == null) {
            throw new IllegalArgumentException("Trainer team cannot be empty or null");
        }
        Long trainerId = newTrainerTeam.getTrainer().getTrainerId();
        Trainer existingTrainer = trainerService.getTrainerById(trainerId);
        newTrainerTeam.setTrainer(existingTrainer);

        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), TrainerTeam.class);
        }
        TrainerTeam createdTeam = ttRepo.save(newTrainerTeam);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdTeam),
                    HttpStatus.CREATED.value());
        }
        return createdTeam;
    }

    @Transactional
    public TrainerTeam editTrainerTeam(Long trainerTeamId, TrainerTeam editedTrainerTeam) {
        return ttRepo.findById(trainerTeamId).map(existingTeam -> {
            existingTeam.setTrainer(editedTrainerTeam.getTrainer());
            existingTeam.setPokemons(editedTrainerTeam.getPokemons());

            return ttRepo.save(existingTeam);
        })
                .orElseThrow(() -> new EntityNotFoundException("No trainer team found with id: " + trainerTeamId));
    }

    public void deleteTrainerTeamById(Long trainerTeamId) {
        if (!ttRepo.existsById(trainerTeamId)) {
            throw new EntityNotFoundException("No trainer team found with id: " + trainerTeamId);
        }
        ttRepo.deleteById(trainerTeamId);
    }
}

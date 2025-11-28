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
import nuzlocke.domain.Route;
import nuzlocke.domain.Trainer;
import nuzlocke.repository.TrainerRepository;

@Service
public class TrainerService {

    // TODO: FETCH TEAMS BY TRAINER??

    private final TrainerRepository trainerRepo;
    private final IdempotencyRecordService idempotencyService;
    private final RouteService routeService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TrainerService(TrainerRepository trainerRepo, IdempotencyRecordService idempotencyService,
            RouteService routeService,
            ObjectMapper objectMapper) {
        this.trainerRepo = trainerRepo;
        this.idempotencyService = idempotencyService;
        this.routeService = routeService;
        this.objectMapper = objectMapper;
    }

    public Iterable<Trainer> getAllTrainers() {
        return trainerRepo.findAll();
    }

    public Trainer getTrainerById(Long trainerId) {
        return trainerRepo.findById(trainerId)
                .orElseThrow(() -> new EntityNotFoundException("No trainer found with id: " + trainerId));
    }

    @Transactional
    public Trainer createNewTrainer(String key, Trainer newTrainer)
            throws JsonMappingException, JsonProcessingException {
        Trainer existingTrainer = trainerRepo.findByTrainerName(newTrainer.getTrainerName());
        if (existingTrainer != null && existingTrainer.getTrainerName().equals(newTrainer.getTrainerName())
                && existingTrainer.getRoute().equals(newTrainer.getRoute())) {
            throw new IllegalArgumentException("Trainer with name: " + existingTrainer.getTrainerName()
                    + " already exists on this route: " + existingTrainer.getRoute().getRouteName());
        }
        Long routeId = newTrainer.getRoute().getRouteId();
        Route existingRoute = routeService.getRouteById(routeId);
        newTrainer.setRoute(existingRoute);

        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), Trainer.class);
        }
        Trainer createdTrainer = trainerRepo.save(newTrainer);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdTrainer),
                    HttpStatus.CREATED.value());
        }
        return createdTrainer;
    }

    @Transactional
    public Trainer editTrainer(Long trainerId, Trainer editedTrainer) {
        return trainerRepo.findById(trainerId).map(existingTrainer -> {
            existingTrainer.setRoute(editedTrainer.getRoute());
            existingTrainer.setTrainerName(editedTrainer.getTrainerName());
            existingTrainer.setTrainerTeams(editedTrainer.getTrainerTeams());

            return trainerRepo.save(existingTrainer);
        })
                .orElseThrow(() -> new EntityNotFoundException("Trainer not found with id" + trainerId));
    }

    public void deleteTrainerById(Long trainerId) {
        if (!trainerRepo.existsById(trainerId)) {
            throw new EntityNotFoundException("Trainer not found with id: " + trainerId);
        }
        trainerRepo.deleteById(trainerId);
    }

}

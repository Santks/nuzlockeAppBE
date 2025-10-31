package nuzlocke.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import nuzlocke.domain.Trainer;
import nuzlocke.service.TrainerService;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private static final Logger log = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        log.info("fetch all trainers");
        return ResponseEntity.ok((List<Trainer>) trainerService.getAllTrainers());
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable Long trainerId) {
        log.info("searching for trainer with id: " + trainerId);
        Trainer existingTrainer = trainerService.getTrainerById(trainerId);
        return ResponseEntity.ok(existingTrainer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Trainer> createTrainer(@RequestHeader(value = "Idempotency-Key", required = false) String key,
            @RequestBody Trainer newTrainer) throws JsonMappingException, JsonProcessingException {
        log.info("adding trainer with name: " + newTrainer.getTrainerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.createNewTrainer(key, newTrainer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{trainerId}")
    public ResponseEntity<Trainer> editTrainer(@PathVariable Long trainerId, @RequestBody Trainer editedTrainer) {
        log.info("saving changes to trainer with id: " + trainerId);
        return ResponseEntity.ok(trainerService.editTrainer(trainerId, editedTrainer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{trainerId}")
    public ResponseEntity<Void> deleteTrainerById(@PathVariable Long trainerId) {
        log.info("deleting trainer with id: " + trainerId);
        trainerService.deleteTrainerById(trainerId);
        return ResponseEntity.noContent().build();
    }
}

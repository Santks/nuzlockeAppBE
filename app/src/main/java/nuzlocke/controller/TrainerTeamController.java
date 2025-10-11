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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nuzlocke.domain.TrainerTeam;
import nuzlocke.service.TrainerTeamService;

@RestController
@RequestMapping("/trainerteams")
public class TrainerTeamController {

    private static final Logger log = LoggerFactory.getLogger(TrainerController.class);

    private final TrainerTeamService ttService;

    @Autowired
    public TrainerTeamController(TrainerTeamService ttService) {
        this.ttService = ttService;
    }

    @GetMapping
    public ResponseEntity<List<TrainerTeam>> getAllTrainerTeams() {
        log.info("fetching all trainer teams");
        return ResponseEntity.ok((List<TrainerTeam>) ttService.getAllTrainerTeams());
    }

    @GetMapping("/{trainerTeamId}")
    public ResponseEntity<TrainerTeam> getTeamById(@PathVariable Long trainerTeamId) {
        log.info("fetch trainer team with id: " + trainerTeamId);
        TrainerTeam existingTeam = ttService.getTrainerTeamById(trainerTeamId);
        return ResponseEntity.ok(existingTeam);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TrainerTeam> createTrainerTeam(@RequestBody TrainerTeam newTrainerTeam) {
        log.info("creating new team for trainer: " + newTrainerTeam.getTrainer().getTrainerName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ttService.createNewTrainerTeam(newTrainerTeam));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{trainerTeamId}")
    public ResponseEntity<TrainerTeam> editTrainerTeam(@PathVariable Long trainerTeamId,
            @RequestBody TrainerTeam editedTrainerTeam) {
        log.info("saving changes to trainer team with id: " + trainerTeamId);
        return ResponseEntity.ok(ttService.editTrainerTeam(trainerTeamId, editedTrainerTeam));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{trainerTeamId}")
    public ResponseEntity<Void> deleteTrainerTeamById(@PathVariable Long trainerTeamId) {
        log.info("deleting trainer team with id: " + trainerTeamId);
        ttService.deleteTrainerTeamById(trainerTeamId);
        return ResponseEntity.noContent().build();
    }

}

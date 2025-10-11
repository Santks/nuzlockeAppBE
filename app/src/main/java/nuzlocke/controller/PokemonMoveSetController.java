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

import nuzlocke.domain.PokemonMoveSet;
import nuzlocke.service.PokemonMoveSetService;

@RestController
@RequestMapping("/movesets")
public class PokemonMoveSetController {

    private static final Logger log = LoggerFactory.getLogger(PokemonMoveSetController.class);

    private final PokemonMoveSetService moveSetService;

    @Autowired
    public PokemonMoveSetController(PokemonMoveSetService moveSetService) {
        this.moveSetService = moveSetService;
    }

    @GetMapping()
    public ResponseEntity<List<PokemonMoveSet>> getAllMoveSets() {
        log.info("fetch all move sets");
        return ResponseEntity.ok((List<PokemonMoveSet>) moveSetService.getAllMoveSets());
    }

    @GetMapping("/{moveSetId}")
    public ResponseEntity<PokemonMoveSet> getMoveSetById(@PathVariable Long moveSetId) {
        log.info("searching for move set with id: " + moveSetId);
        PokemonMoveSet existingMoveSet = moveSetService.findMovesetById(moveSetId);
        return ResponseEntity.ok(existingMoveSet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PokemonMoveSet> createMoveSet(@RequestBody PokemonMoveSet newMoveSet) {
        log.info("creating new move set for pokemon: " + newMoveSet.getPokemon().getPokemonName());
        return ResponseEntity.status(HttpStatus.CREATED).body(moveSetService.createNewMoveSet(newMoveSet));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{moveSetId}")
    public ResponseEntity<PokemonMoveSet> editMoveSet(@PathVariable Long moveSetId,
            @RequestBody PokemonMoveSet editedMoveSet) {
        log.info("editing move set with id: " + moveSetId);
        return ResponseEntity.ok(moveSetService.editMoveSet(moveSetId, editedMoveSet));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{moveSetId}")
    public ResponseEntity<Void> deleteMoveSet(@PathVariable Long moveSetId) {
        log.info("deleting move set with id: " + moveSetId);
        moveSetService.deleteMoveSetById(moveSetId);
        return ResponseEntity.noContent().build();
    }
}

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

import nuzlocke.domain.Pokemon;
import nuzlocke.service.PokemonService;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private static final Logger log = LoggerFactory.getLogger(PokemonController.class);

    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public ResponseEntity<List<Pokemon>> getAllPokemon() {
        log.info("fetch all pokemon");
        return ResponseEntity.ok((List<Pokemon>) pokemonService.getAllPokemon());
    }

    @GetMapping("/{pokemonId}")
    public ResponseEntity<Pokemon> getPokemonById(@PathVariable Long pokemonId) {
        log.info("searching for pokemon with id: " + pokemonId);
        Pokemon existingPokemon = pokemonService.findPokemonById(pokemonId);
        return ResponseEntity.ok(existingPokemon);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Pokemon> createNewPokemon(
            @RequestHeader(value = "Idempotency-Key", required = false) String key, @RequestBody Pokemon newPokemon)
            throws JsonMappingException, JsonProcessingException {
        log.info("creating new pokemon with name: " + newPokemon.getPokemonName());
        return ResponseEntity.status(HttpStatus.CREATED).body(pokemonService.createNewPokemon(key, newPokemon));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{pokemonId}")
    public ResponseEntity<Pokemon> editPokemon(@PathVariable Long pokemonId, @RequestBody Pokemon editedPokemon) {
        log.info("editing pokemon with id: " + pokemonId);
        return ResponseEntity.ok(pokemonService.editPokemon(pokemonId, editedPokemon));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Void> deletePokemonById(@PathVariable Long pokemonId) {
        log.info("deleting pokemon with id: " + pokemonId);
        pokemonService.deletePokemonById(pokemonId);
        return ResponseEntity.noContent().build();
    }

}

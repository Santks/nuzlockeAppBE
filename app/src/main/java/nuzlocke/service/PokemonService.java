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
import nuzlocke.domain.Pokemon;
import nuzlocke.domain.TrainerTeam;
import nuzlocke.repository.PokemonRepository;

@Service
public class PokemonService {

    // TODO: MAKE GET MOVESETS BY POKEMON

    private final PokemonRepository pokemonRepo;
    private final IdempotencyRecordService idempotencyService;
    private final TrainerTeamService ttService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PokemonService(PokemonRepository pokemonRepo, IdempotencyRecordService idempotencyService,
            TrainerTeamService ttService,
            ObjectMapper objectMapper) {
        this.pokemonRepo = pokemonRepo;
        this.idempotencyService = idempotencyService;
        this.ttService = ttService;
        this.objectMapper = objectMapper;
    }

    public Iterable<Pokemon> getAllPokemon() {
        return pokemonRepo.findAll();
    }

    public Pokemon findPokemonById(Long pokemonId) {
        return pokemonRepo.findById(pokemonId)
                .orElseThrow(() -> new EntityNotFoundException("No pokemon found with id: " + pokemonId));
    }

    @Transactional
    public Pokemon createNewPokemon(String key, Pokemon newPokemon)
            throws JsonMappingException, JsonProcessingException {
        Pokemon existingPokemon = pokemonRepo.findByPokemonNameIgnoreCase(newPokemon.getPokemonName());
        if (existingPokemon != null) {
            throw new IllegalArgumentException("This pokemon already exists.");
        }
        Long trainerTeamId = newPokemon.getTrainerTeam().getTrainerTeamId();
        TrainerTeam existingTeam = ttService.getTrainerTeamById(trainerTeamId);
        newPokemon.setTrainerTeam(existingTeam);

        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), Pokemon.class);
        }
        Pokemon createdPokemon = pokemonRepo.save(newPokemon);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdPokemon),
                    HttpStatus.CREATED.value());
        }
        return createdPokemon;
    }

    @Transactional
    public Pokemon editPokemon(Long pokemonId, Pokemon editedPokemon) {
        return pokemonRepo.findById(pokemonId).map(existingPokemon -> {
            existingPokemon.setPokemonName(editedPokemon.getPokemonName());
            existingPokemon.setAbilities(editedPokemon.getAbilities());
            existingPokemon.setType1(editedPokemon.getType1());
            existingPokemon.setType2(editedPokemon.getType2());
            existingPokemon.setTrainerTeam(editedPokemon.getTrainerTeam());
            existingPokemon.setMoveSets(editedPokemon.getMoveSets());

            return pokemonRepo.save(existingPokemon);
        })
                .orElseThrow(() -> new EntityNotFoundException("No pokemon found with id: " + pokemonId));
    }

    public void deletePokemonById(Long pokemonId) {
        if (!pokemonRepo.existsById(pokemonId)) {
            throw new EntityNotFoundException("No pokemon found with id: " + pokemonId);
        }
        pokemonRepo.deleteById(pokemonId);
    }
}

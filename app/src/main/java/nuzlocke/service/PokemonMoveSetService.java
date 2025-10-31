package nuzlocke.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.domain.PokemonMoveSet;
import nuzlocke.repository.PokemonMoveSetRepository;

@Service
public class PokemonMoveSetService {

    private final PokemonMoveSetRepository moveSetRepo;
    private final IdempotencyRecordService idempotencyService;
    private final ObjectMapper objectMapper;

    @Autowired
    public PokemonMoveSetService(PokemonMoveSetRepository moveSetRepo, IdempotencyRecordService idempotencyService,
            ObjectMapper objectMapper) {
        this.moveSetRepo = moveSetRepo;
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;
    }

    public Iterable<PokemonMoveSet> getAllMoveSets() {
        return moveSetRepo.findAll();
    }

    public PokemonMoveSet findMovesetById(Long moveSetId) {
        return moveSetRepo.findById(moveSetId)
                .orElseThrow(() -> new EntityNotFoundException("No move set found with id: " + moveSetId));
    }

    @Transactional
    public PokemonMoveSet createNewMoveSet(String key, PokemonMoveSet newMoveSet)
            throws JsonMappingException, JsonProcessingException {
        List<PokemonMoveSet> existingMoveSet = moveSetRepo
                .findByPokemon_PokemonName(newMoveSet.getPokemon().getPokemonName());
        for (PokemonMoveSet existing : existingMoveSet) {
            if (existing.getItem().equals(newMoveSet.getItem()) && existing.getMoves().equals(newMoveSet.getMoves())
                    && existing.getNature().equals(newMoveSet.getNature())
                    && existing.getPokemonLevel() == newMoveSet.getPokemonLevel()) {
                throw new IllegalArgumentException("This pokemon already has this move set.");
            }
        }
        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), PokemonMoveSet.class);
        }

        PokemonMoveSet createdMoveSet = moveSetRepo.save(newMoveSet);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdMoveSet),
                    HttpStatus.CREATED.value());
        }
        return createdMoveSet;
    }

    @Transactional
    public PokemonMoveSet editMoveSet(Long moveSetId, PokemonMoveSet editedMoveSet) {
        return moveSetRepo.findById(moveSetId).map(existingMoveSet -> {
            existingMoveSet.setItem(editedMoveSet.getItem());
            existingMoveSet.setMoves(editedMoveSet.getMoves());
            existingMoveSet.setNature(editedMoveSet.getNature());
            existingMoveSet.setPokemon(editedMoveSet.getPokemon());
            existingMoveSet.setPokemonLevel(editedMoveSet.getPokemonLevel());

            return moveSetRepo.save(existingMoveSet);
        })
                .orElseThrow(() -> new EntityNotFoundException("No move set found with id: " + moveSetId));
    }

    public void deleteMoveSetById(Long moveSetId) {
        if (!moveSetRepo.existsById(moveSetId)) {
            throw new EntityNotFoundException("No move set found with id: " + moveSetId);
        }
        moveSetRepo.deleteById(moveSetId);
    }
}

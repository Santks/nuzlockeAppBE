package nuzlocke.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.PokemonMoveSet;
import nuzlocke.repository.PokemonMoveSetRepository;

@Service
public class PokemonMoveSetService {

    private final PokemonMoveSetRepository moveSetRepo;

    @Autowired
    public PokemonMoveSetService(PokemonMoveSetRepository moveSetRepo) {
        this.moveSetRepo = moveSetRepo;
    }

    public Iterable<PokemonMoveSet> getAllMoveSets() {
        return moveSetRepo.findAll();
    }

    public PokemonMoveSet findMovesetById(@PathVariable Long moveSetId) {
        return moveSetRepo.findById(moveSetId)
                .orElseThrow(() -> new EntityNotFoundException("No move set found with id: " + moveSetId));
    }

    public PokemonMoveSet createNewMoveSet(@RequestBody PokemonMoveSet newMoveSet) {
        List<PokemonMoveSet> existingMoveSet = moveSetRepo
                .findByPokemon_PokemonName(newMoveSet.getPokemon().getPokemonName());
        for (PokemonMoveSet existing : existingMoveSet) {
            if (existing.getItem().equals(newMoveSet.getItem()) && existing.getMoves().equals(newMoveSet.getMoves())
                    && existing.getNature().equals(newMoveSet.getNature())
                    && existing.getPokemonLevel() == newMoveSet.getPokemonLevel()) {
                throw new IllegalArgumentException("This pokemon already has this move set.");
            }
        }
        return moveSetRepo.save(newMoveSet);
    }

    @Transactional
    public PokemonMoveSet editMoveSet(@PathVariable Long moveSetId, @RequestBody PokemonMoveSet editedMoveSet) {
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

    public void deleteMoveSetById(@PathVariable Long moveSetId) {
        if (!moveSetRepo.existsById(moveSetId)) {
            throw new EntityNotFoundException("No move set found with id: " + moveSetId);
        }
        moveSetRepo.deleteById(moveSetId);
    }
}

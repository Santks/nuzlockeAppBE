package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Pokemon;
import nuzlocke.repository.PokemonRepository;

@Service
public class PokemonService {

    // TODO: MAKE GET MOVESETS BY POKEMON

    private final PokemonRepository pokemonRepo;

    @Autowired
    public PokemonService(PokemonRepository pokemonRepo) {
        this.pokemonRepo = pokemonRepo;
    }

    public Iterable<Pokemon> getAllPokemon() {
        return pokemonRepo.findAll();
    }

    public Pokemon findPokemonById(@PathVariable Long pokemonId) {
        return pokemonRepo.findById(pokemonId)
                .orElseThrow(() -> new EntityNotFoundException("No pokemon found with id: " + pokemonId));
    }

    public Pokemon createNewPokemon(@RequestBody Pokemon newPokemon) {
        Pokemon existingPokemon = pokemonRepo.findByPokemonNameIgnoreCase(newPokemon.getPokemonName());
        if (existingPokemon != null) {
            throw new IllegalArgumentException("This pokemon already exists.");
        }
        return pokemonRepo.save(newPokemon);
    }

    @Transactional
    public Pokemon editPokemon(@PathVariable Long pokemonId, @RequestBody Pokemon editedPokemon) {
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

    public void deletePokemonById(@PathVariable Long pokemonId) {
        if (!pokemonRepo.existsById(pokemonId)) {
            throw new EntityNotFoundException("No pokemon found with id: " + pokemonId);
        }
        pokemonRepo.deleteById(pokemonId);
    }
}

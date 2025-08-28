package nuzlocke.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Game;
import nuzlocke.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepo;

    public Iterable<Game> getAllGames() {
        return gameRepo.findAll();
    }

    public Optional<Game> getGameById(Long gameId) {
        if (!gameRepo.existsById(gameId)) {
            throw new EntityNotFoundException("No game found with id: " + gameId);
        }
        return gameRepo.findById(gameId);
    }

    public Game createNewGame(Game newGame) {
        return gameRepo.save(newGame);
    }

    public Game editGame(Long gameId, Game editedGame) {
        return gameRepo.findById(gameId).map(existingGame -> {
            existingGame.setTitle(editedGame.getTitle());
            existingGame.setDeveloper(editedGame.getDeveloper());
            existingGame.setDescription(editedGame.getDescription());
            existingGame.setGameGeneration(editedGame.getGameGeneration());
            existingGame.setRegions(editedGame.getRegions());

            return gameRepo.save(existingGame);
        })
                .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    }

    public void deleteGame(Long gameId) {
        if (!gameRepo.existsById(gameId)) {
            throw new EntityNotFoundException("No game found with id " + gameId);
        }
        gameRepo.deleteById(gameId);
    }

}

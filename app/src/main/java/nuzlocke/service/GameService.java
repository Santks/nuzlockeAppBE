package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Game getGameById(Long gameId) {
        return gameRepo.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    }

    public Game createNewGame(Game newGame) {
        return gameRepo.save(newGame);
    }

    @Transactional
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

    @Transactional
    public void deleteGame(Long gameId) {
        if (!gameRepo.existsById(gameId)) {
            throw new EntityNotFoundException("No game found with id " + gameId);
        }
        gameRepo.deleteById(gameId);
    }

}

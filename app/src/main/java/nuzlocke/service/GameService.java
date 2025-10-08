package nuzlocke.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Game;
import nuzlocke.domain.Region;
import nuzlocke.domain.Route;
import nuzlocke.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepo;

    @Autowired
    public GameService(GameRepository gameRepo) {
        this.gameRepo = gameRepo;

    }

    public Iterable<Game> getAllGames() {
        return gameRepo.findAll();
    }

    public Game getGameById(Long gameId) {
        return gameRepo.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    }

    public Game createNewGame(Game newGame) {
        Game existingGame = gameRepo.findByTitle(newGame.getTitle());
        if (existingGame != null) {
            throw new IllegalArgumentException("Game with title " + existingGame.getTitle() + " already exists");
        }
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

    public void deleteGame(Long gameId) {
        if (!gameRepo.existsById(gameId)) {
            throw new EntityNotFoundException("No game found with id " + gameId);
        }
        gameRepo.deleteById(gameId);
    }

    public List<Route> getAllGameRoutes(Long gameId) {
        Game existingGame = gameRepo.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
        List<Region> gameRegion = existingGame.getRegions();
        List<Route> routes = gameRegion.stream().flatMap(region -> region.getRoutes().stream())
                .toList();
        return routes;
    };
}

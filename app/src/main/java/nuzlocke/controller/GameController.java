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

import nuzlocke.domain.Game;
import nuzlocke.domain.Route;
import nuzlocke.service.GameService;

@RestController
@RequestMapping("/games")
public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        log.info("Fetch all games");
        return ResponseEntity.ok((List<Game>) gameService.getAllGames());
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        log.info("Searching for game with id: " + gameId);
        Game existingGame = gameService.getGameById(gameId);
        return ResponseEntity.ok(existingGame);
    }

    @GetMapping("/{gameId}/routes")
    public ResponseEntity<List<Route>> getGameRoutes(@PathVariable Long gameId) {
        log.info("fetch routes for game with id: " + gameId);
        return ResponseEntity.ok((List<Route>) gameService.getAllGameRoutes(gameId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Game> newGame(@RequestHeader(value = "Idempotency-Key", required = false) String key,
            @RequestBody Game newGame) throws JsonMappingException, JsonProcessingException {
        log.info("Adding new game: " + newGame.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createNewGame(key, newGame));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{gameId}")
    public ResponseEntity<Game> editGame(@PathVariable Long gameId, @RequestBody Game existingGame) {
        log.info("Saving changes to game with id: " + gameId);
        return ResponseEntity.ok(gameService.editGame(gameId, existingGame));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        log.info("Deleting game with id: " + gameId);
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

}

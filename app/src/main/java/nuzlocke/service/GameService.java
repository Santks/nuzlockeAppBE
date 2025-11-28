package nuzlocke.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Game;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.domain.Region;
import nuzlocke.domain.Route;
import nuzlocke.repository.GameRepository;
import nuzlocke.repository.RegionRepository;

@Service
public class GameService {

    private final GameRepository gameRepo;
    private final RegionRepository regionRepo;
    private final IdempotencyRecordService idempotencyService;
    private final ObjectMapper objectMapper;

    @Autowired
    public GameService(GameRepository gameRepo, RegionRepository regionRepo,
            IdempotencyRecordService idempotencyService,
            ObjectMapper objectMapper) {
        this.gameRepo = gameRepo;
        this.regionRepo = regionRepo;
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;

    }

    public Iterable<Game> getAllGames() {
        return gameRepo.findAll();
    }

    public Game getGameById(Long gameId) {
        return gameRepo.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    }

    @Transactional
    public Game createNewGame(String key, Game newGame) throws JsonMappingException, JsonProcessingException {
        Game existingGame = gameRepo.findByTitle(newGame.getTitle());
        if (existingGame != null) {
            throw new IllegalArgumentException("Game with title " + existingGame.getTitle() + " already exists");
        }
        Long regionId = newGame.getRegions().get(0).getRegionId();
        if (regionId != null) {
            Region existingRegion = regionRepo.findById(regionId)
                    .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
            List<Region> regions = new ArrayList<>();
            regions.add(existingRegion);
            newGame.setRegions(regions);
        }
        IdempotencyRecord existing = idempotencyService.fetchOrReserve(key);
        if (existing != null && existing.getResponse() != null) {
            return objectMapper.readValue(existing.getResponse(), Game.class);
        }
        Game createdGame = gameRepo.save(newGame);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdGame),
                    HttpStatus.CREATED.value());
        }
        return createdGame;
    }

    @Transactional
    public Game editGame(Long gameId, Game editedGame) {
        return gameRepo.findById(gameId).map(existingGame -> {
            existingGame.setTitle(editedGame.getTitle());
            existingGame.setDeveloper(editedGame.getDeveloper());
            existingGame.setDescription(editedGame.getDescription());
            existingGame.setGameGeneration(editedGame.getGameGeneration());
            List<Region> regions = new ArrayList<>();
            Long regionId = editedGame.getRegions().get(0).getRegionId();
            if (regionId != null) {
                Region existingRegion = regionRepo.findById(regionId)
                        .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
                regions.add(existingRegion);
            }
            existingGame.setRegions(regions);
            Game savedGame = gameRepo.save(existingGame);

            return savedGame;
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

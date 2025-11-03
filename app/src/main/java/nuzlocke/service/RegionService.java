package nuzlocke.service;

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
import nuzlocke.repository.RegionRepository;

@Service
public class RegionService {

    // TODO: GET ROUTES BY REGION??

    private final RegionRepository regionRepo;
    private final IdempotencyRecordService idempotencyService;
    private final GameService gameService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RegionService(RegionRepository regionRepo, IdempotencyRecordService idempotencyService,
            GameService gameService,
            ObjectMapper objectMapper) {
        this.regionRepo = regionRepo;
        this.idempotencyService = idempotencyService;
        this.gameService = gameService;
        this.objectMapper = objectMapper;
    }

    public Iterable<Region> getAllRegions() {
        return regionRepo.findAll();
    }

    public Region getRegionById(Long regionId) {
        return regionRepo.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
    }

    @Transactional
    public Region addNewRegion(String key, Region newRegion) throws JsonMappingException, JsonProcessingException {
        Region existingRegion = regionRepo.findByRegionName(newRegion.getRegionName());
        if (existingRegion != null) {
            throw new IllegalArgumentException(
                    "Region with name " + existingRegion.getRegionName() + " already exists");
        }
        Long gameId = newRegion.getGame().getGameId();
        Game existingGame = gameService.getGameById(gameId);
        newRegion.setGame(existingGame);

        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), Region.class);
        }
        Region createdRegion = regionRepo.save(newRegion);
        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdRegion),
                    HttpStatus.CREATED.value());
        }
        return createdRegion;
    }

    @Transactional
    public Region editRegion(Long regionId, Region editedRegion) {
        return regionRepo.findById(regionId).map(existingRegion -> {
            existingRegion.setRegionName(editedRegion.getRegionName());
            existingRegion.setGame(editedRegion.getGame());
            existingRegion.setRoutes(editedRegion.getRoutes());

            return regionRepo.save(existingRegion);
        })
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
    }

    public void deleteRegionById(Long regionId) {
        if (!regionRepo.existsById(regionId)) {
            throw new EntityNotFoundException("No region found with id: " + regionId);
        }
        regionRepo.deleteById(regionId);
    }

}

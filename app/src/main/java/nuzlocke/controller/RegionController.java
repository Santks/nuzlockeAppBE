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

import nuzlocke.domain.Region;
import nuzlocke.service.RegionService;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private static final Logger log = LoggerFactory.getLogger(RegionController.class);

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public ResponseEntity<List<Region>> getAllRegions() {
        log.info("Fetch all regions");
        return ResponseEntity.ok((List<Region>) regionService.getAllRegions());
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long regionId) {
        log.info("Fetch region with id: " + regionId);
        Region existingRegion = regionService.getRegionById(regionId);
        return ResponseEntity.ok(existingRegion);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Region> createNewRegion(
            @RequestHeader(value = "Idempotency-Key", required = false) String key, @RequestBody Region newRegion)
            throws JsonMappingException, JsonProcessingException {
        log.info("Creating new region: " + newRegion.getRegionName());
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.addNewRegion(key, newRegion));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{regionId}")
    public ResponseEntity<Region> editRegion(@PathVariable Long regionId, @RequestBody Region existingRegion) {
        log.info("Saving changes to region with id: " + regionId);
        return ResponseEntity.ok(regionService.editRegion(regionId, existingRegion));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{regionId}")
    public ResponseEntity<Void> deleteRegionById(@PathVariable Long regionId) {
        log.info("Deleting region with id: " + regionId);
        regionService.deleteRegionById(regionId);
        return ResponseEntity.noContent().build();
    }
}

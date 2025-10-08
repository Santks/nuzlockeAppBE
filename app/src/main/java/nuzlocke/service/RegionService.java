package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Region;
import nuzlocke.repository.RegionRepository;

@Service
public class RegionService {

    private final RegionRepository regionRepo;

    @Autowired
    public RegionService(RegionRepository regionRepo) {
        this.regionRepo = regionRepo;
    }

    public Iterable<Region> getAllRegions() {
        return regionRepo.findAll();
    }

    public Region getRegionById(Long regionId) {
        return regionRepo.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("No region found with id: " + regionId));
    }

    public Region addNewRegion(Region newRegion) {
        Region existingRegion = regionRepo.findByRegionName(newRegion.getRegionName());
        if (existingRegion != null) {
            throw new IllegalArgumentException(
                    "Region with name " + existingRegion.getRegionName() + " already exists");
        }
        return regionRepo.save(newRegion);
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

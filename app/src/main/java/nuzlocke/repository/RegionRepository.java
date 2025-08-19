package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;
import nuzlocke.domain.Region;

public interface RegionRepository extends CrudRepository<Region, Long> {

}

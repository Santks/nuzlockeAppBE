package nuzlocke.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.Route;

public interface RouteRepository extends CrudRepository<Route, Long> {
    Route findByRouteName(String routeName);

    List<Route> findByRegion_RegionId(Long regionId);
}

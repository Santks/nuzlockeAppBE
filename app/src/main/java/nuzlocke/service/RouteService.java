package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.Route;
import nuzlocke.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepo;

    @Autowired
    public RouteService(RouteRepository routeRepo) {
        this.routeRepo = routeRepo;
    }

    public Iterable<Route> getAllRoutes() {
        return routeRepo.findAll();
    }

    public Route getRouteById(Long routeId) {
        return routeRepo.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("No route found with id: " + routeId));
    }

    public Route createNewRoute(Route newRoute) {
        Route existingRoute = routeRepo.findByRouteName(newRoute.getRouteName());
        if (existingRoute != null && existingRoute.getRouteName().equals(newRoute.getRouteName())
                && existingRoute.getRegion().equals(newRoute.getRegion())) {
            throw new IllegalArgumentException("Route with name: " + existingRoute.getRouteName()
                    + " already exists in region: " + existingRoute.getRegion().getRegionName());
        }
        return routeRepo.save(newRoute);
    }

    @Transactional
    public Route editRoute(Long routeId, Route editedRoute) {
        return routeRepo.findById(routeId).map(existingRoute -> {
            existingRoute.setRouteName(editedRoute.getRouteName());
            existingRoute.setRegion(editedRoute.getRegion());
            existingRoute.setTrainers(editedRoute.getTrainers());

            return routeRepo.save(existingRoute);
        })
                .orElseThrow(() -> new EntityNotFoundException("No route found with id: " + routeId));
    }

    public void deleteRoute(Long routeId) {
        if (!routeRepo.existsById(routeId)) {
            throw new EntityNotFoundException("No route found with id: " + routeId);
        }
        routeRepo.deleteById(routeId);
    }
}

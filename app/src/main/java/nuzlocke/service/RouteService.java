package nuzlocke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.domain.Route;
import nuzlocke.repository.RouteRepository;

@Service
public class RouteService {

    // TODO: GET TRAINERS BY ROUTE

    private final RouteRepository routeRepo;
    private final IdempotencyRecordService idempotencyService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RouteService(RouteRepository routeRepo, IdempotencyRecordService idempotencyService,
            ObjectMapper objectMapper) {
        this.routeRepo = routeRepo;
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;
    }

    public Iterable<Route> getAllRoutes() {
        return routeRepo.findAll();
    }

    public Route getRouteById(Long routeId) {
        return routeRepo.findById(routeId)
                .orElseThrow(() -> new EntityNotFoundException("No route found with id: " + routeId));
    }

    @Transactional
    public Route createNewRoute(String key, Route newRoute) throws JsonMappingException, JsonProcessingException {
        Route existingRoute = routeRepo.findByRouteName(newRoute.getRouteName());
        if (existingRoute != null && existingRoute.getRouteName().equals(newRoute.getRouteName())
                && existingRoute.getRegion().equals(newRoute.getRegion())) {
            throw new IllegalArgumentException("Route with name: " + existingRoute.getRouteName()
                    + " already exists in region: " + existingRoute.getRegion().getRegionName());
        }
        IdempotencyRecord existingRecord = idempotencyService.fetchOrReserve(key);
        if (existingRecord != null && existingRecord.getResponse() != null) {
            return objectMapper.readValue(existingRecord.getResponse(), Route.class);
        }
        Route createdRoute = routeRepo.save(newRoute);

        if (key != null && !key.isBlank()) {
            idempotencyService.saveRecord(key, objectMapper.writeValueAsString(createdRoute),
                    HttpStatus.CREATED.value());
        }

        return createdRoute;
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

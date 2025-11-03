package nuzlocke.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import nuzlocke.domain.Route;
import nuzlocke.service.RouteService;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private static final Logger log = LoggerFactory.getLogger(RouteController.class);

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        log.info("Fetching all routes");
        return ResponseEntity.ok((List<Route>) routeService.getAllRoutes());
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long routeId) {
        log.info("Fetching route with id: " + routeId);
        Route existingRoute = routeService.getRouteById(routeId);
        return ResponseEntity.ok(existingRoute);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Route> createNewRoute(@RequestHeader(value = "Idempotency-Key", required = false) String key,
            @RequestBody Route newRoute) throws JsonMappingException, JsonProcessingException {
        log.info("Adding new route: " + newRoute.getRouteName());
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createNewRoute(key, newRoute));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{routeId}")
    public ResponseEntity<Route> editRoute(@PathVariable Long routeId, @RequestBody Route existingRoute) {
        log.info("Saving changes to route with id: " + routeId);
        return ResponseEntity.ok(routeService.editRoute(routeId, existingRoute));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRouteById(@PathVariable Long routeId) {
        log.info("Deleting route with id: " + routeId);
        routeService.deleteRoute(routeId);
        return ResponseEntity.noContent().build();
    }
}

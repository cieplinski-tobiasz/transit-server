package application.rest.controllers;

import application.domain.Leg;
import application.domain.Route;
import application.domain.Stop;
import application.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/routes")
public class RouteRestController {
    private final RouteService routeService;

    @Autowired
    public RouteRestController(RouteService routeService) {
        Assert.notNull(routeService, "RouteService cannot be null.");
        this.routeService = routeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Route> readAllRoutes() {
        return routeService.getAllRoutes();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{routeId}")
    Optional<Route> readRouteById(@PathVariable Long routeId) {
        return routeService.getRouteById(routeId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addRoute(@RequestBody Route route) {
        if (routeService.addRoute(route)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(route.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}

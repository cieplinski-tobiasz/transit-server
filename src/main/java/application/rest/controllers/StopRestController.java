package application.rest.controllers;

import application.domain.Stop;
import application.services.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/stops")
public class StopRestController {
    private final StopService stopService;

    @Autowired
    public StopRestController(StopService stopService) {
        Assert.notNull(stopService, "StopService cannot be null.");
        this.stopService = stopService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Stop> readAllStops() {
        return stopService.getAllStops();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{stopId}")
    Optional<Stop> readStopById(@PathVariable Long stopId) {
        return stopService.getStopById(stopId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addStop(@RequestBody Stop stop) {
        if (stopService.addStop(stop)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(stop.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

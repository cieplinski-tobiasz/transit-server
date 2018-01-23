package application.rest.controllers;

import application.domain.Leg;
import application.services.LegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/legs")
public class LegRestController {
    private final LegService legService;

    @Autowired
    public LegRestController(LegService legService) {
        Assert.notNull(legService, "LegService cannot be null.");
        this.legService = legService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Leg> readAllLegs() {
        return legService.getAllLegs();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{legId}")
    Optional<Leg> readLegById(@PathVariable Long legId) {
        return legService.getLegById(legId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addLeg(@RequestBody Leg leg) {
        if (legService.addLeg(leg)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(leg.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}

package application.rest.controllers;

import application.domain.Timetable;
import application.domain.Travel;
import application.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Month;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/travels")
public class TravelRestController {
    private final TravelService travelService;

    @Autowired
    public TravelRestController(TravelService travelService) {
        Assert.notNull(travelService, "TravelService cannot be null.");
        this.travelService = travelService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Travel> getAllTravels() {
        return travelService.getAllTravels();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{travelId}")
    Optional<Travel> getTravelById(@PathVariable Long travelId) {
        return travelService.getTravelById(travelId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{month}")
    Collection<Travel> getTravelByMonth(@PathVariable Month month) {
        return travelService.getTravelsByMonth(month);
    }

    @RequestMapping(method = RequestMethod.GET, params = {"driverId", "month"})
    Collection<Travel> getTravelByDriverIdAndMonth(@RequestParam Long driverId, @RequestParam Month month) {
        return travelService.getTravelsByDriverAndMonth(driverId, month);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addTravel(@RequestBody Travel travel) {
        if (travelService.addTravel(travel)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(travel.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<?> setDriverWithIdForTravel(@RequestBody Travel travel) {
        if (travelService.setDriverForTravelWithId(travel.getId(), travel.getDriver().getId())) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(travel.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

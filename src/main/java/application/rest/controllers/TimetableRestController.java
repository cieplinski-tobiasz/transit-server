package application.rest.controllers;

import application.domain.Timetable;
import application.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/timetables")
public class TimetableRestController {
    private final TimetableService timetableService;

    @Autowired
    public TimetableRestController(TimetableService timetableService) {
        Assert.notNull(timetableService, "TimetableService cannot be null.");
        this.timetableService = timetableService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Timetable> readAllTimetables() {
        return timetableService.getAllTimetables();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{timetableId}")
    Optional<Timetable> readTimetableById(@PathVariable Long timetableId) {
        return timetableService.getTimetableById(timetableId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addTimetable(@RequestBody Timetable timetable) {
        if (timetableService.addTimetable(timetable)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(timetable.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

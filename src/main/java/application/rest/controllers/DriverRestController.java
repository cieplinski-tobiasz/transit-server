package application.rest.controllers;

import application.domain.Driver;
import application.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
public class DriverRestController {
    private final DriverService driverService;

    @Autowired
    public DriverRestController(DriverService driverService) {
        Assert.notNull(driverService, "DriverService cannot be null.");
        this.driverService = driverService;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Driver> readAllDrivers() {
        return driverService.getAllDrivers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{driverId}")
    Optional<Driver> readDriverById(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addDriver(@RequestBody Driver driver) {
        if (driverService.addDriver(driver)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(driver.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

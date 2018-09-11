package application.services;

import application.Main;
import application.repositories.VehicleRepository;
import application.domain.Vehicle;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class VehicleServiceTest {
    @Autowired
    private VehicleRepository vehicleRepository;
    private List<Vehicle> vehicles;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();

        vehicles = new ArrayList<Vehicle>() {{
            add(new Vehicle("v0", 10));
            add(new Vehicle("v1", 20));
            add(new Vehicle("v2", 30));
        }};

        vehicleRepository.saveAll(vehicles);
    }

    @AfterEach
    void cleanUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    void getAllVehiclesTest() {
        VehicleService vehicleService = new VehicleService(vehicleRepository);

        List<Vehicle> result = vehicleService.getAllVehicles();

        Assert.assertThat(result, IsIterableContainingInAnyOrder.containsInAnyOrder(vehicles.toArray(new Vehicle[3])));
    }

    @Test
    void getVehicleByIdGivenVehicleIsPresentTest() {
        VehicleService vehicleService = new VehicleService(vehicleRepository);

        Optional<Vehicle> result = vehicleService.getVehicleById(vehicles.get(0).getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(vehicles.get(0), result.get())
        );
    }

    @Test
    void getVehicleByIdGivenVehicleIsNotPresentTest() {
        VehicleService vehicleService = new VehicleService(vehicleRepository);
        Vehicle noSuchVehicle = new Vehicle("no such vehicle", 40);

        Optional<Vehicle> result = vehicleService.getVehicleById(noSuchVehicle.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void addVehicleGivenVehicleIsNotNullAndIsNotPresentTest() {
        VehicleService vehicleService = new VehicleService(vehicleRepository);
        Vehicle noSuchVehicle = new Vehicle("no such vehicle", 40);;

        boolean result = vehicleService.addVehicle(noSuchVehicle);

        assertAll(
                () -> assertTrue(result),
                () -> assertTrue(vehicleService.getVehicleById(noSuchVehicle.getId()).isPresent())
        );
    }

    @Test
    void addVehicleGivenDriverIsNullTest() {
        VehicleService driverService = new VehicleService(vehicleRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> driverService.addVehicle(null)
        );
    }

    @Test
    void addVehicleGivenVehicleIsPresentTest() {
        VehicleService vehicleService = new VehicleService(vehicleRepository);

        boolean result = vehicleService.addVehicle(vehicles.get(0));

        assertFalse(result);
    }

}
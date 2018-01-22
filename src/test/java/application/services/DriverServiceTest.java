package application.services;

import application.Main;
import application.dao.DriverRepository;
import application.domain.Driver;
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
public class DriverServiceTest {
    @Autowired
    private DriverRepository driverRepository;
    private List<Driver> drivers;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();

        drivers = new ArrayList<Driver>() {{
            add(new Driver("name0", "12345"));
            add(new Driver("name1", "23456"));
            add(new Driver("name2", "34567"));
        }};

        driverRepository.saveAll(drivers);
    }

    @AfterEach
    void cleanUp() {
        driverRepository.deleteAll();
    }

    @Test
    void getAllDriversTest() {
        DriverService driverService = new DriverService(driverRepository);

        List<Driver> result = driverService.getAllDrivers();

        Assert.assertThat(result, IsIterableContainingInAnyOrder.containsInAnyOrder(drivers.toArray(new Driver[3])));
    }

    @Test
    void getDriverByIdGivenDriverIsPresentTest() {
        DriverService driverService = new DriverService(driverRepository);

        Optional<Driver> result = driverService.getDriverById(drivers.get(0).getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(drivers.get(0), result.get())
        );
    }

    @Test
    void getDriverByIdGivenDriverIsNotPresentTest() {
        DriverService driverService = new DriverService(driverRepository);
        Driver noSuchDriver = new Driver("no such driver", "12345");

        Optional<Driver> result = driverService.getDriverById(noSuchDriver.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void addDriverGivenDriverIsNotNullAndIsNotPresentTest() {
        DriverService driverService = new DriverService(driverRepository);
        Driver noSuchDriver = new Driver("no such driver", "12345");

        boolean result = driverService.addDriver(noSuchDriver);

        assertAll(
                () -> assertTrue(result),
                () -> assertTrue(driverService.getDriverById(noSuchDriver.getId()).isPresent())
        );
    }

    @Test
    void addDriverGivenDriverIsNullTest() {
        DriverService driverService = new DriverService(driverRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> driverService.addDriver(null)
        );
    }

    @Test
    void addDriverGivenDriverIsPresentTest() {
        DriverService driverService = new DriverService(driverRepository);

        boolean result = driverService.addDriver(drivers.get(0));

        assertFalse(result);
    }

}
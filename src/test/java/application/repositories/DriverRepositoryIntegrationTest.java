package application.repositories;


import application.Main;
import application.domain.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class DriverRepositoryIntegrationTest {
    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldSaveDriverIntegrationTest() {
        Driver driver = new Driver("name", "123456");
        driverRepository.save(driver);

        Optional<Driver> result = driverRepository.findById(driver.getId());

        assertAll(
                () -> assertTrue(driverRepository.existsById(driver.getId())),
                () -> assertEquals(driver, driverRepository.findById(driver.getId()).get())
        );
    }

    @Test
    @Transactional
    void shouldDeleteDriverIntegrationTest() {
        Driver driver = new Driver("name", "123456");
        driverRepository.save(driver);
        driverRepository.delete(driver);

        assertFalse(driverRepository.existsById(driver.getId()));
    }

}
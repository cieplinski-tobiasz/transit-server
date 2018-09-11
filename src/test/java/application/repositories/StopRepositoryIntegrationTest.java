package application.repositories;

import application.Main;
import application.domain.Location;
import application.domain.Stop;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class StopRepositoryIntegrationTest {
    @Autowired
    private StopRepository stopRepository;

    private Stop fromWithoutDescription;
    private Stop toWithoutDescription;
    private Stop fromWithDescription;
    private Stop toWithDescription;

    @BeforeEach
    public void setUp() throws Exception {
        stopRepository.deleteAll();

        fromWithoutDescription = new Stop(
                "fromWithoutDescription-stop",
                new Location("fromWithoutDescription-address")
        );
        toWithoutDescription = new Stop(
                "toWithoutDescription-stop",
                new Location("toWithoutDescription-address")
        );
        fromWithDescription = new Stop(
                "fromWithDescription-stop",
                "from-description",
                new Location("fromWithDescription-address")
        );
        toWithDescription = new Stop(
                "toWithDescription-stop",
                "to-description",
                new Location("toWithDescription-address")
        );
    }

    @Test
    @Transactional
    public void shouldSaveStopWithNoDescriptionIntegrationTest() throws Exception {
        Stop stopWithoutDescription = new Stop(
                "stopWithoutDescription",
                new Location("stopWithoutDescription-address")
        );

        stopRepository.save(stopWithoutDescription);
        Optional<Stop> findResult = stopRepository.findById(stopWithoutDescription.getId());

        assertAll(
                () -> assertTrue(findResult.isPresent()),
                () -> assertEquals(stopWithoutDescription, findResult.get())
        );
    }

    @Test
    @Transactional
    public void shouldSaveStopWithDescriptionIntegrationTest() throws Exception {
        Stop stopWithDescription = new Stop(
                "stopWithoutDescription",
                "description",
                new Location("stopWithoutDescription-address")
        );

        stopRepository.save(stopWithDescription);
        Optional<Stop> findResult = stopRepository.findById(stopWithDescription.getId());

        assertAll(
                () -> assertTrue(findResult.isPresent()),
                () -> assertEquals(stopWithDescription, findResult.get())
        );
    }

    @Test
    @Transactional
    public void shouldDeleteStopWithNoDescriptionAndLocationIntegrationTest() throws Exception {
        Location location = new Location("stopWithoutDescription-address");
        Stop stopWithoutDescription = new Stop(
                "stopWithoutDescription",
                location
        );
        stopRepository.save(stopWithoutDescription);

        stopRepository.delete(stopWithoutDescription);

        assertAll(
                () -> assertFalse(stopRepository.existsById(stopWithoutDescription.getId()))
        );
    }

    @Test
    @Transactional
    public void shouldDeleteStopWithDescriptionAndLocationIntegrationTest() throws Exception {
        Location location = new Location("stopWithDescription-address");
        Stop stopWithoutDescription = new Stop(
                "stopWithDescription",
                "description",
                location
        );
        stopRepository.save(stopWithoutDescription);

        stopRepository.delete(stopWithoutDescription);

        assertAll(
                () -> assertFalse(stopRepository.existsById(stopWithoutDescription.getId()))
        );
    }


}
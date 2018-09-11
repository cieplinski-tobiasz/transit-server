package application.services;

import application.Main;
import application.repositories.StopRepository;
import application.domain.Location;
import application.domain.Stop;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class StopServiceTest {
    @Autowired
    private StopRepository stopRepository;
    private List<Stop> stops;

    @BeforeEach
    void setUp() {
        stopRepository.deleteAll();

        stops = new LinkedList<Stop>() {{
            add(new Stop("name0", "desc0", new Location("loc0")));
            add(new Stop("name1", "desc1", new Location("loc1")));
            add(new Stop("name2", "desc2", new Location("loc2")));
        }};

        stopRepository.saveAll(stops);
    }

    @AfterEach
    void cleanUp() {
        stopRepository.deleteAll();
    }

    @Test
    void getAllStopsTest() {
        StopService stopService = new StopService(stopRepository);

        List<Stop> result = stopService.getAllStops();

        Assert.assertThat(result, IsIterableContainingInAnyOrder.containsInAnyOrder(stops.toArray(new Stop[3])));
    }

    @Test
    void getStopByIdGivenStopPresentTest() {
        StopService stopService = new StopService(stopRepository);

        Optional<Stop> result = stopService.getStopById(stops.get(0).getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(stops.get(0), result.get())
        );
    }

    @Test
    void getStopByIdGivenStopIsNotPresentTest() {
        StopService stopService = new StopService(stopRepository);
        Stop noSuchStop = new Stop("no such stop", new Location("no such loc"));

        Optional<Stop> result = stopService.getStopById(noSuchStop.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void addStopGivenStopIsNotPresentTest() {
        StopService stopService = new StopService(stopRepository);
        Stop noSuchStop = new Stop("no such stop", new Location("no such loc"));

        boolean result = stopService.addStop(noSuchStop);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(noSuchStop, stopService.getStopById(noSuchStop.getId()).get())
        );
    }

    @Test
    void addStopGivenStopIsPresentTest() {
        StopService stopService = new StopService(stopRepository);

        boolean result = stopService.addStop(stops.get(0));

        assertFalse(result);
    }

    @Test
    void addStopGivenStopIsNullTest() {
        StopService stopService = new StopService(stopRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> stopService.addStop(null)
        );
    }


}
package application.repositories;

import application.Main;
import application.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class RouteRepositoryIntegrationTest {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private StopRepository stopRepository;

    private List<Leg> legs;

    @BeforeEach
    public void setUp() throws Exception {
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();

        Stop[] stops = {
                new Stop("stop1", new Location("loc1")),
                new Stop("stop2", new Location("loc2")),
                new Stop("stop3", new Location("loc3"))
        };

        legs = Arrays.asList(
                new Leg(stops[0], stops[1], new Distance(10)),
                new Leg(stops[1], stops[2], new Distance(20))
        );

        stopRepository.saveAll(Arrays.asList(stops));
    }

    @Test
    @Transactional
    void saveRouteGivenLegsPresentIntegrationTest() {
        legRepository.saveAll(legs);
        Route route = new Route(legs);
        routeRepository.save(route);

        Optional<Route> result = routeRepository.findById(route.getId());

        assertAll(
                () -> assertTrue(routeRepository.existsById(route.getId())),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(route, result.get())
        );
    }

    @Test
    @Transactional
    void deleteRouteAndDoNotDeleteStopsOrLegsIntegrationTest() {
        legRepository.saveAll(legs);
        Route route = new Route(legs);
        routeRepository.save(route);

        routeRepository.delete(route);

        assertAll(
                () -> assertFalse(routeRepository.existsById(route.getId())),
                () -> assertTrue(legRepository.existsById(legs.get(0).getId())),
                () -> assertTrue(legRepository.existsById(legs.get(1).getId())),
                () -> assertTrue(stopRepository.existsById(legs.get(0).getDepartureStop().getId())),
                () -> assertTrue(stopRepository.existsById(legs.get(0).getArrivalStop().getId())),
                () -> assertTrue(stopRepository.existsById(legs.get(1).getDepartureStop().getId())),
                () -> assertTrue(stopRepository.existsById(legs.get(1).getArrivalStop().getId()))
        );
    }
}
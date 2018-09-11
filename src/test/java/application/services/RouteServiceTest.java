package application.services;

import application.Main;
import application.repositories.LegRepository;
import application.repositories.RouteRepository;
import application.repositories.StopRepository;
import application.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class RouteServiceTest {
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private RouteRepository routeRepository;

    private Route route;

    @BeforeEach
    void setUp() {
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();

        List<Stop> stops = new ArrayList<Stop>() {{
                add(new Stop("0", new Location("l0")));
                add(new Stop("1", new Location("l1")));
                add(new Stop("2", new Location("l2")));
        }};

        stopRepository.saveAll(stops);
        List<Leg> legs = new ArrayList<Leg>() {{
            add(new Leg(stops.get(0), stops.get(1), new Distance(10)));
            add(new Leg(stops.get(1), stops.get(2), new Distance(20)));
        }};

        legRepository.saveAll(legs);
        route = new Route(legs);
    }

    @AfterEach
    void cleanUp() {
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();
    }

    @Test
    @Transactional
    void getAllRoutesTest() {
        RouteService routeService = new RouteService(routeRepository);
        routeService.addRoute(route);

        List<Route> result = routeService.getAllRoutes();

        assertThat(result, containsInAnyOrder(route));
    }

    @Test
    @Transactional
    void getRouteByIdGivenRoutePresentTest() {
        RouteService routeService = new RouteService(routeRepository);
        routeService.addRoute(route);

        Optional<Route> result = routeService.getRouteById(route.getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(route, result.get())
        );
    }

    @Test
    @Transactional
    void getRouteByIdGivenRouteIsNotPresentTest() {
        RouteService routeService = new RouteService(routeRepository);

        Optional<Route> result = routeService.getRouteById(route.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    void addRouteGivenNoRoutePresentTest() {
        RouteService routeService = new RouteService(routeRepository);

        boolean addResult = routeService.addRoute(route);
        Optional<Route> result = routeService.getRouteById(route.getId());

        assertAll(
                () -> assertTrue(addResult),
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(route, result.get())
        );
    }

    @Test
    void addRouteGivenRouteIsAlreadyPresentTest() {
        RouteService routeService = new RouteService(routeRepository);
        routeService.addRoute(route);

        boolean result = routeService.addRoute(route);

        assertFalse(result);
    }

    @Test
    void addRouteGivenNullArgumentTest() {
        RouteService routeService = new RouteService(routeRepository);

        assertThrows(
                IllegalArgumentException.class, () -> routeService.addRoute(null)
        );
    }


}

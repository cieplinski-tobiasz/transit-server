package application.domain;

import application.exceptions.NoSuchSubRouteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class RouteTest {
    @Mock
    private Location location;
    @Mock
    private Distance distance;
    private List<Leg> legs;
    private Stop[] stops;
    private Stop noSuchStop;

    @BeforeEach
    public void setUp() throws Exception {
        stops = new Stop[]{
                new Stop("0", "d0", location),
                new Stop("1", "d1", location),
                new Stop("2", "d2", location),
                new Stop("3", "d3", location)
        };
        noSuchStop = new Stop("No such", "stop", location);
        legs = new LinkedList<>();

        for (int i = 0; i + 1 < stops.length; ++i) {
            legs.add(new Leg(stops[i], stops[i + 1], distance));
        }
    }

    @Test
    void getLegForDepartureStopGivenNullArgumentTest() throws Exception {
        Route route = new Route(legs);

        Optional<Leg> result = route.getLegForDepartureStop(null);

        assertFalse(result.isPresent());
    }

    @Test
    void getLegForDepartureStopGivenNoStopFoundTest() throws Exception {
        Route route = new Route(legs);

        Optional<Leg> result = route.getLegForDepartureStop(noSuchStop);

        assertFalse(result.isPresent());
    }

    @Test
    void getLegForDepartureStopGivenStopFoundTest() throws Exception {
        Route route = new Route(legs);
        Stop departureStop = stops[0];

        Optional<Leg> result = route.getLegForDepartureStop(departureStop);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), legs.get(0))
        );
    }

    @Test
    void getLegForArrivalStopGivenNoStopFoundTest() throws Exception {
        Route route = new Route(legs);

        Optional<Leg> result = route.getLegForArrivalStop(noSuchStop);

        assertFalse(result.isPresent());
    }

    @Test
    void getLegForArrivalStopGivenStopFoundTest() throws Exception {
        Route route = new Route(legs);
        Stop arrivalStop = stops[1];

        Optional<Leg> result = route.getLegForArrivalStop(arrivalStop);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), legs.get(0))
        );
    }

    @Test
    void getLegForArrivalStopGivenNullArgumentTest() throws Exception {
        Route route = new Route(legs);

        Optional<Leg> result = route.getLegForArrivalStop(null);

        assertFalse(result.isPresent());
    }

    @Test
    void makeSubRouteWhenArgumentsAreValidTest() throws Exception {
        Route route = new Route(legs);
        Stop departureStop = stops[1];
        Stop arrivalStop = stops[3];

        Route result = route.makeSubRoute(departureStop, arrivalStop);

        assertIterableEquals(legs.subList(1, 3), result.getLegs());
    }

    @Test
    void makeSubRouteWhenArgumentsAreNullTest() throws Exception {
        Route route = new Route(legs);

        assertThrows(NoSuchSubRouteException.class, () -> route.makeSubRoute(null, null));
    }

    @Test
    void makeSubRouteWhenArgumentsAreInBadOrderTest() throws Exception {
        Route route = new Route(legs);
        Stop invalidDepartureStop = stops[3];
        Stop invalidArrivalStop = stops[1];

        assertThrows(NoSuchSubRouteException.class, () -> route.makeSubRoute(invalidDepartureStop, invalidArrivalStop));
    }

    @Test
    void makeSubRouteWhenStopIsNotPresentInRouteTest() throws Exception {
        Route route = new Route(legs);
        Stop departureStop = stops[3];
        Stop invalidArrivalStop = noSuchStop;

        assertThrows(NoSuchSubRouteException.class, () -> route.makeSubRoute(departureStop, invalidArrivalStop));
    }
}
package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class TimetableTest {
    @Mock
    private Distance distance;
    @Mock
    private Location location;
    private LocalTime localTime;
    private Route route;
    private Map<Leg, LocalTime> departureTimeMap;
    private Map<Leg, LocalTime> arrivalTimeMap;
    private Leg[] legs;
    private Leg noSuchLeg;
    private Stop startStop;

    @BeforeEach
    public void setUp() throws Exception {
        startStop = new Stop("1", "d1", location);
        legs = new Leg[]{
                new Leg(startStop, new Stop("2", "d2", location), distance),
                new Leg(new Stop("2", "d2", location), new Stop("3", "d3", location), distance),
                new Leg(new Stop("3", "d3", location), new Stop("4", "d4", location), distance),
                new Leg(new Stop("4", "d4", location), new Stop("5", "d5", location), distance)
        };

        noSuchLeg = new Leg(new Stop("no such", "dx", location), new Stop("stop", "dx", location), distance);
        localTime = LocalTime.NOON;
        arrivalTimeMap = new HashMap<>();
        departureTimeMap = new HashMap<>();

        Arrays.stream(legs)
                .forEach(l -> {
                    departureTimeMap.put(l, localTime);
                    arrivalTimeMap.put(l, localTime);
                });

        route = new Route(Arrays.asList(legs));
    }

    @Test
    void getDepartureTimeForStopGivenNoStopFoundTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForStop(noSuchLeg.getArrivalStop());

        assertFalse(result.isPresent());
    }

    @Test
    void getDepartureTimeForStopGivenStopFoundTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForStop(startStop);

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), localTime)
        );
    }

    @Test
    void getDepartureTimeForStopGivenNullArgumentTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForStop(null);

        assertFalse(result.isPresent());
    }

    @Test
    void getArrivalTimeForStopGivenNoStopFoundTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForStop(noSuchLeg.getArrivalStop());

        assertFalse(result.isPresent());
    }

    @Test
    void getArrivalTimeForStopGivenStopFoundTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForStop(legs[0].getArrivalStop());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), localTime)
        );
    }

    @Test
    void getArrivalTimeForStopGivenNullArgumentTest() {
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForStop(null);

        assertFalse(result.isPresent());
    }

    @Test
    void getDepartureTimeForLegGivenNoLegFoundTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForLeg(noSuchLeg);

        assertFalse(result.isPresent());
    }

    @Test
    void getDepartureTimeForLegGivenLegFoundTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForLeg(legs[0]);

        assertAll(() -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), localTime)
        );
    }

    @Test
    void getDepartureTimeForLegGivenNullArgumentTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getDepartureTimeForLeg(null);

        assertFalse(result.isPresent());
    }

    @Test
    void getArrivalTimeForLegGivenNoLegFoundTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForLeg(noSuchLeg);

        assertFalse(result.isPresent());
    }

    @Test
    void getArrivalTimeForLegGivenLegFoundTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForLeg(legs[0]);

        assertAll(() -> assertTrue(result.isPresent()),
                () -> assertEquals(result.get(), localTime)
        );
    }

    @Test
    void getArrivalTimeForLegGivenNullArgumentTest() {
        Timetable timetable = new Timetable(route, arrivalTimeMap, departureTimeMap);

        Optional<LocalTime> result = timetable.getArrivalTimeForLeg(null);

        assertFalse(result.isPresent());
    }
}
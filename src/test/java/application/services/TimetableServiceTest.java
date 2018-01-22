package application.services;

import application.Main;
import application.dao.LegRepository;
import application.dao.RouteRepository;
import application.dao.StopRepository;
import application.dao.TimetableRepository;
import application.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class TimetableServiceTest {
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TimetableRepository timetableRepository;

    private Timetable timetable;

    @BeforeEach
    void setUp() {
        timetableRepository.deleteAll();
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
        Route route = new Route(legs);
        routeRepository.save(route);

        Map<Leg, LocalTime> departureTimeMap = new HashMap<>();
        departureTimeMap.put(legs.get(1), LocalTime.NOON);
        departureTimeMap.put(legs.get(0), LocalTime.NOON);

        Map<Leg, LocalTime> arrivalTimeMap = new HashMap<>();
        departureTimeMap.put(legs.get(1), LocalTime.MIDNIGHT);
        departureTimeMap.put(legs.get(0), LocalTime.MIDNIGHT);

        timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);
    }

    @AfterEach
    void cleanUp() {
        timetableRepository.deleteAll();
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();
    }

    @Test
    @Transactional
    void getAllTimetablesTest() {
        timetableRepository.save(timetable);
        TimetableService timetableService = new TimetableService(timetableRepository);

        List<Timetable> result = timetableService.getAllTimetables();

        assertThat(result, containsInAnyOrder(timetable));
    }

    @Test
    @Transactional
    void getTimetableByIdGivenTimetablePresentTest() {
        timetableRepository.save(timetable);
        TimetableService timetableService = new TimetableService(timetableRepository);

        Optional<Timetable> result = timetableService.getTimetableById(timetable.getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(timetable, result.get())
        );
    }

    @Test
    @Transactional
    void getTimetableByIdGivenTimetableIsNotPresentTest() {
        TimetableService timetableService = new TimetableService(timetableRepository);

        Optional<Timetable> result = timetableService.getTimetableById(timetable.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    void getTimetableByIdGivenNullArgumentTest() {
        TimetableService timetableService = new TimetableService(timetableRepository);

        assertThrows(IllegalArgumentException.class, () -> timetableService.getTimetableById(null));
    }

    @Test
    @Transactional
    void addTimetableByIdGivenTimetableIsNotPresentTest() {
        TimetableService timetableService = new TimetableService(timetableRepository);

        boolean result = timetableService.addTimetable(timetable);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(timetable, timetableService.getTimetableById(timetable.getId()).get())
        );
    }

    @Test
    @Transactional
    void addTimetableByIdGivenTimetableIsPresentTest() {
        TimetableService timetableService = new TimetableService(timetableRepository);
        timetableService.addTimetable(timetable);

        boolean result = timetableService.addTimetable(timetable);

        assertFalse(result);
    }

    @Test
    @Transactional
    void addTimetableByIdGivenNullArgumentTest() {
        TimetableService timetableService = new TimetableService(timetableRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> timetableService.addTimetable(null)
        );
    }


}

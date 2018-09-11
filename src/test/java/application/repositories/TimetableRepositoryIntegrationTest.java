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

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class TimetableRepositoryIntegrationTest {
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TimetableRepository timetableRepository;

    private Route route;
    private Map<Leg, LocalTime> departureTimeMap;
    private Map<Leg, LocalTime> arrivalTimeMap;

    @BeforeEach
    void setUp() {
        stopRepository.deleteAll();
        legRepository.deleteAll();
        routeRepository.deleteAll();
        timetableRepository.deleteAll();

        Stop[] stops = {
                new Stop("stop1", new Location("loc1")),
                new Stop("stop2", new Location("loc2")),
                new Stop("stop3", new Location("loc3"))
        };

        stopRepository.saveAll(Arrays.asList(stops));

        List<Leg> legs = Arrays.asList(
                new Leg(stops[0], stops[1], new Distance(10)),
                new Leg(stops[1], stops[2], new Distance(20))
        );

        legRepository.saveAll(legs);

        route = new Route(legs);

        departureTimeMap = new HashMap<>();
        departureTimeMap.put(legs.get(0), LocalTime.NOON);
        departureTimeMap.put(legs.get(1), LocalTime.NOON);

        arrivalTimeMap = new HashMap<>();
        arrivalTimeMap.put(legs.get(0), LocalTime.MIDNIGHT);
        arrivalTimeMap.put(legs.get(1), LocalTime.MIDNIGHT);
    }

    @Test
    @Transactional
    void shouldSaveTimetableGivenRoutePresentIntegrationTest() {
        routeRepository.save(route);
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);
        timetableRepository.save(timetable);

        Optional<Timetable> result = timetableRepository.findById(timetable.getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(timetable, result.get())
        );
    }

    @Test
    @Transactional
    void shouldDeleteTimetableAndNotDeleteRouteIntegrationTest() {
        routeRepository.save(route);
        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);
        timetableRepository.save(timetable);

        timetableRepository.delete(timetable);

        assertAll(
                () -> assertFalse(timetableRepository.existsById(timetable.getId())),
                () -> assertTrue(routeRepository.existsById(route.getId()))
        );
    }


}
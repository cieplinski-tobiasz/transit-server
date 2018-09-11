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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class TravelRepositoryIntegrationTest {
    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private LegRepository legRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TimetableRepository timetableRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DriverRepository driverRepository;

    private Timetable timetable;
    private LocalDate departureDate;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        stopRepository.deleteAll();
        legRepository.deleteAll();
        routeRepository.deleteAll();
        timetableRepository.deleteAll();
        travelRepository.deleteAll();

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

        Route route = new Route(legs);

        routeRepository.save(route);

        Map<Leg, LocalTime> departureTimeMap = new HashMap<>();
        departureTimeMap.put(legs.get(0), LocalTime.NOON);
        departureTimeMap.put(legs.get(1), LocalTime.NOON);

        Map<Leg, LocalTime> arrivalTimeMap = new HashMap<>();
        arrivalTimeMap.put(legs.get(0), LocalTime.MIDNIGHT);
        arrivalTimeMap.put(legs.get(1), LocalTime.MIDNIGHT);

        timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);
        departureDate = LocalDate.of(2018, 1, 22);
        vehicle = new Vehicle("name", 20);
    }

    @Test
    @Transactional
    void shouldSaveTravelGivenTimetableAndVehiclePresentIntegrationTest() {
        timetableRepository.save(timetable);
        vehicleRepository.save(vehicle);
        Travel travel = new Travel(timetable, departureDate, vehicle);
        travelRepository.save(travel);

        Optional<Travel> result = travelRepository.findById(travel.getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(travel, result.get())
        );
    }

    @Test
    @Transactional
    void shouldUpdateTravelGivenDriverIsPresentIntegrationTest() {
        timetableRepository.save(timetable);
        vehicleRepository.save(vehicle);
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Driver driver = new Driver("name", "12345");
        driverRepository.save(driver);
        travelRepository.save(travel);

        Optional<Travel> result = travelRepository.findById(travel.getId());
        result.ifPresent(t -> {
            t.setDriver(driver);
            travelRepository.save(t);
        });

        assertAll(
                () -> assertTrue(travelRepository.existsById(travel.getId())),
                () -> assertEquals(driver, travelRepository.findById(travel.getId()).get().getDriver())
        );

    }

    @Test
    @Transactional
    void shouldDeleteTravelAndNotDeleteTimetableOrVehicleIntegrationTest() {
        timetableRepository.save(timetable);
        vehicleRepository.save(vehicle);
        Travel travel = new Travel(timetable, departureDate, vehicle);
        travelRepository.save(travel);

        travelRepository.delete(travel);

        assertAll(
                () -> assertFalse(travelRepository.existsById(travel.getId())),
                () -> assertTrue(timetableRepository.existsById(timetable.getId())),
                () -> assertTrue(vehicleRepository.existsById(vehicle.getId()))
        );
    }

}
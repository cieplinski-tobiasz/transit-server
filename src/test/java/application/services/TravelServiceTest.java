package application.services;

import application.Main;
import application.dao.*;
import application.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class TravelServiceTest {
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
    private DriverRepository driverRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    private List<Travel> travels;
    private Driver driver;
    private Travel noSuchTravel;

    @BeforeEach
    void setUp() {
        travelRepository.deleteAll();
        vehicleRepository.deleteAll();
        timetableRepository.deleteAll();
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();
        driverRepository.deleteAll();

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

        Timetable timetable = new Timetable(route, departureTimeMap, arrivalTimeMap);
        timetableRepository.save(timetable);
        Vehicle vehicle = new Vehicle("vehicle", 50);
        vehicleRepository.save(vehicle);

        driver = new Driver("driver", "123");
        driverRepository.save(driver);

        travels = new ArrayList<Travel>() {{
            add(new Travel(timetable, LocalDate.of(2012, 12, 12), vehicle));
            add(new Travel(timetable, LocalDate.of(2012, 12, 13), vehicle));
            add(new Travel(timetable, LocalDate.of(2012, 12, 14), vehicle));
            add(new Travel(timetable, LocalDate.of(2012, 12, 15), vehicle));
        }};

        noSuchTravel = new Travel(timetable, LocalDate.of(2018, 12, 14), vehicle);

        travels.get(0).setDriver(driver);
        travels.get(1).setDriver(driver);
        travelRepository.saveAll(travels);
    }

    @AfterEach
    void cleanUp() {
        travelRepository.deleteAll();
        vehicleRepository.deleteAll();
        timetableRepository.deleteAll();
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    @Transactional
    void getAllTravelsTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> allTravels = travelService.getAllTravels();

        assertThat(allTravels, containsInAnyOrder(travels.toArray(new Travel[4])));
    }

    @Test
    @Transactional
    void getTravelByIdGivenTravelPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        Optional<Travel> result = travelService.getTravelById(travels.get(0).getId());

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(travels.get(0), result.get())
        );
    }

    @Test
    @Transactional
    void getTravelByIdGivenTravelIsNotPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        Optional<Travel> result = travelService.getTravelById(noSuchTravel.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @Transactional
    void getTravelByIdGivenArgumentIsNullTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.getTravelById(null)
        );
    }

    @Test
    @Transactional
    void getTravelsByDriverGivenDriverIsPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> result = travelService.getTravelsByDriver(driver);

        assertThat(result, containsInAnyOrder(travels.subList(0, 2).toArray(new Travel[2])));
    }

    @Test
    @Transactional
    void getTravelsByDriverGivenDriverIsNotPresentTest() {
        TravelService travelService = new TravelService(travelRepository);
        Driver noSuchDriver = new Driver("no such driver", "1234");

        List<Travel> result = travelService.getTravelsByDriver(noSuchDriver);

        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void getTravelsByDriverGivenNullArgumentTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.getTravelsByDriver(null)
        );
    }

    @Test
    @Transactional
    void getTravelsByMonthGivenTravelsPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> result = travelService.getTravelsByMonth(Month.DECEMBER);

        assertThat(result, containsInAnyOrder(travels.toArray(new Travel[4])));
    }

    @Test
    @Transactional
    void getTravelsByMonthGivenTravelsNotPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> result = travelService.getTravelsByMonth(Month.APRIL);

        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void getTravelsByMonthGivenNullArgumentTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.getTravelsByMonth(null)
        );
    }

    @Test
    @Transactional
    void getTravelsByDriverAndMonthGivenValidArgumentsTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> result = travelService.getTravelsByDriverAndMonth(driver, Month.DECEMBER);

        assertThat(result, containsInAnyOrder(travels.subList(0, 2).toArray(new Travel[2])));
    }

    @Test
    @Transactional
    void getTravelsByDriverAndMonthGivenNullArgumentsTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.getTravelsByDriverAndMonth(null, null)
        );
    }

    @Test
    @Transactional
    void getTravelsByDriverAndMonthGivenNoSuchTravelsTest() {
        TravelService travelService = new TravelService(travelRepository);

        List<Travel> result = travelService.getTravelsByDriverAndMonth(driver, Month.APRIL);

        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void addTravelGivenTravelPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        boolean result = travelService.addTravel(travels.get(0));

        assertFalse(result);
    }

    @Test
    @Transactional
    void addTravelGivenTravelIsNotPresentTest() {
        TravelService travelService = new TravelService(travelRepository);

        boolean result = travelService.addTravel(noSuchTravel);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(noSuchTravel, travelService.getTravelById(noSuchTravel.getId()).get())
        );
    }

    @Test
    @Transactional
    void addTravelGivenNullArgumentTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.addTravel(null)
        );
    }

    @Test
    @Transactional
    void setDriverForTravelGivenNullArgumentsTest() {
        TravelService travelService = new TravelService(travelRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> travelService.setDriverForTravelWithId(null, null)
        );
    }

    @Test
    @Transactional
    void setDriverForTravelGivenNoSuchTravelTest() {
        TravelService travelService = new TravelService(travelRepository);


        boolean result = travelService.setDriverForTravelWithId(noSuchTravel.getId(), driver);

        assertFalse(result);
    }

    @Test
    @Transactional
    void setDriverForTravelGivenValidArgumentsTest() {
        Driver newDriver = new Driver("driver2", "123456");
        driverRepository.save(newDriver);
        TravelService travelService = new TravelService(travelRepository);

        boolean result = travelService.setDriverForTravelWithId(travels.get(0).getId(), newDriver);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(newDriver, travelService.getTravelById(travels.get(0).getId()).get().getDriver())
        );

    }


}

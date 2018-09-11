package application.services;

import application.Main;
import application.repositories.*;
import application.domain.*;
import org.junit.jupiter.api.AfterEach;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class BookingServiceTest {
    @Autowired
    private BookingRepository bookingRepository;
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
    private Booking booking;

    @BeforeEach
    void setUp() {
        travelRepository.deleteAll();
        bookingRepository.deleteAll();
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

        booking = new Booking(new Client("name", "phone"), stops.get(0), stops.get(1), 2);
    }

    @AfterEach
    void cleanUp() {
        travelRepository.deleteAll();
        bookingRepository.deleteAll();
        vehicleRepository.deleteAll();
        timetableRepository.deleteAll();
        routeRepository.deleteAll();
        legRepository.deleteAll();
        stopRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    @Transactional
    void addBookingsAndGetBookingsByTravelIdGivenPresentTravelTest() {
        BookingService bookingService = new BookingService(bookingRepository, travelRepository);

        boolean result = bookingService.addBookingToTravelById(booking, travels.get(0).getId());

        assertAll(
                () -> assertTrue(result),
                () -> assertThat(travelRepository.findById(travels.get(0).getId()).get().getBookings(),
                        containsInAnyOrder(booking)),
                () -> assertTrue(bookingService.getBookingsByTravelId(travels.get(0).getId()).contains(booking))
        );
    }

    @Test
    @Transactional
    void removeBookingFromTravelByIdGivenPresentTravelAndBookingTest() {
        BookingService bookingService = new BookingService(bookingRepository, travelRepository);
        bookingService.addBookingToTravelById(booking, travels.get(0).getId());

        boolean result = bookingService.removeBookingFromTravelById(booking, travels.get(0).getId());

        assertAll(
                () -> assertTrue(result),
                () -> assertTrue(travelRepository.findById(travels.get(0).getId()).get().getBookings().isEmpty()),
                () -> assertFalse(bookingService.getBookingsByTravelId(travels.get(0).getId()).contains(booking))
        );
    }

    // TODO: More test cases
}
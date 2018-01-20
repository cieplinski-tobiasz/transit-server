package domain;

import exceptions.NoSuchSubRouteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelTest {
    private Leg[] legs;
    private Stop startStop;
    private Stop midStop;
    private Stop noSuchStop;
    private final int VEHICLE_CAPACITY = 50;
    private Vehicle vehicle;
    private Driver driver;
    private LocalDate departureDate;
    private Timetable timetable;

    @BeforeEach
    void setUp() {
        Location location = mock(Location.class);
        Distance distance = mock(Distance.class);
        vehicle = mock(Vehicle.class);
        when(vehicle.getCapacity()).thenReturn(VEHICLE_CAPACITY);
        driver = mock(Driver.class);
        departureDate = LocalDate.ofYearDay(2012, 24);

        startStop = new Stop("1", "d1", location);
        midStop = new Stop("3", "d3", location);
        legs = new Leg[]{
                new Leg(startStop, new Stop("2", "d2", location), distance),
                new Leg(new Stop("2", "d2", location), midStop, distance),
                new Leg(midStop, new Stop("4", "d4", location), distance),
                new Leg(new Stop("4", "d4", location), new Stop("5", "d5", location), distance)
        };
        Stop noSuchStop = new Stop("no such stop", "dx", location);
        Map<Leg, LocalTime> arrivalTimeMap = new HashMap<>();
        Map<Leg, LocalTime> departureTimeMap = new HashMap<>();
        Arrays.stream(legs)
                .forEach(l -> {
                    departureTimeMap.put(l, LocalTime.NOON);
                    arrivalTimeMap.put(l, LocalTime.MIDNIGHT);
                });

        timetable = new Timetable(new Route(Arrays.asList(legs)), departureTimeMap, arrivalTimeMap);
    }

    @Test
    void availableSeatsRemainingGivenValidArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        int result = travel.availableSeatsRemaining(startStop, midStop);

        assertEquals(VEHICLE_CAPACITY, result);
    }

    @Test
    void availableSeatsRemainingGivenInvalidArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        assertThrows(
                NoSuchSubRouteException.class,
                () -> travel.availableSeatsRemaining(midStop, startStop)
        );
    }

    @Test
    void availableSeatsRemainingGivenFirstArgumentIsNullTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        assertThrows(
                NoSuchSubRouteException.class,
                () -> travel.availableSeatsRemaining(null, midStop)
        );
    }

    @Test
    void availableSeatsRemainingGivenSecondArgumentIsNullTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        assertThrows(
                NoSuchSubRouteException.class,
                () -> travel.availableSeatsRemaining(midStop, null)
        );
    }

    @Test
    void availableSeatsRemainingGivenNullArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        assertThrows(
                NoSuchSubRouteException.class,
                () -> travel.availableSeatsRemaining(null, null)
        );
    }

    @Test
    void addBookingGivenFreeSeatsAvailableAndValidArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Client client = mock(Client.class);
        int seatsBooked = 5;
        Booking booking = new Booking(client, startStop, midStop, seatsBooked);

        boolean result = travel.addBooking(booking);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(VEHICLE_CAPACITY - seatsBooked, travel.availableSeatsRemaining(startStop, midStop)),
                () -> assertTrue(travel.getBookings().contains(booking))
        );
    }

    @Test
    void addBookingGivenNoSeatsAvailableAndValidArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Client client = mock(Client.class);
        int seatsBooked = VEHICLE_CAPACITY + 1;
        Booking booking = new Booking(client, startStop, midStop, seatsBooked);

        boolean result = travel.addBooking(booking);

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(VEHICLE_CAPACITY, travel.availableSeatsRemaining(startStop, midStop)),
                () -> assertFalse(travel.getBookings().contains(booking))
        );
    }

    @Test
    void addBookingGivenInvalidArgumentsTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Client client = mock(Client.class);
        Booking booking = new Booking(client, noSuchStop, midStop, 2);

        boolean result = travel.addBooking(booking);

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(VEHICLE_CAPACITY, travel.availableSeatsRemaining(startStop, midStop)),
                () -> assertFalse(travel.getBookings().contains(booking))
        );
    }

    @Test
    void deleteBookingGivenBookingPresentTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Client client = mock(Client.class);
        int bookedSeats = 2;
        Booking booking = new Booking(client, startStop, midStop, bookedSeats);
        travel.addBooking(booking);

        boolean result = travel.removeBooking(booking);

        assertAll(
                () -> assertTrue(result),
                () -> assertEquals(travel.availableSeatsRemaining(startStop, midStop), VEHICLE_CAPACITY),
                () -> assertFalse(travel.getBookings().contains(booking))
        );
    }

    @Test
    void deleteBookingGivenBookingNotPresentTest() throws Exception {
        Travel travel = new Travel(timetable, departureDate, vehicle);
        Client client = mock(Client.class);
        Booking booking = new Booking(client, startStop, midStop, 2);

        boolean result = travel.removeBooking(booking);

        assertAll(
                () -> assertFalse(result),
                () -> assertEquals(travel.availableSeatsRemaining(startStop, midStop), VEHICLE_CAPACITY),
                () -> assertFalse(travel.getBookings().contains(booking))
        );
    }

    @Test
    void isDefinedWhenDriverIsNotYetSetTest() {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        // no setDriver called

        assertFalse(travel.isDefined());
    }

    @Test
    void isDefinedWhenDriverIsSetTest() {
        Travel travel = new Travel(timetable, departureDate, vehicle);

        travel.setDriver(driver);

        assertTrue(travel.isDefined());
    }
}
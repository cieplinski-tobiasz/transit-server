package application.domain;

import application.exceptions.NoSuchSubRouteException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    @OneToOne
    private final Timetable timetable;
    @NotNull
    private final LocalDate departureDate;
    @OneToMany
    private final Set<Booking> bookings;
    @ElementCollection
    private final Map<Leg, Integer> availableSeats;
    @NotNull
    @OneToOne
    private final Vehicle vehicle;
    @OneToOne
    private Driver driver;

    private Travel() {
        this.timetable = null;
        this.departureDate = null;
        this.bookings = null;
        this.availableSeats = null;
        this.vehicle = null;
        this.driver = null;
    }

    public Travel(Timetable timetable, LocalDate departureDate, Vehicle vehicle) {
        this.timetable = timetable;
        this.departureDate = departureDate;
        this.bookings = new HashSet<>();
        this.driver = null;
        this.vehicle = vehicle;
        this.availableSeats = timetable.getRoute()
                .getLegs()
                .stream()
                .collect(Collectors.toMap(Function.identity(), value -> vehicle.getCapacity()));
    }

    public int availableSeatsRemaining(Stop departure, Stop arrival) throws NoSuchSubRouteException {
        return timetable.getRoute()
                .makeSubRoute(departure, arrival)
                .getLegs()
                .stream()
                .map(availableSeats::get)
                .min(Comparator.naturalOrder())
                .orElseThrow(NoSuchSubRouteException::new);
    }

    public boolean addBooking(Booking booking) {
        try {
            if (availableSeatsRemaining(booking.getFrom(), booking.getTo()) >= booking.getBookedSeats()
                    && bookings.add(booking)) {
                subtractAvailableSeats(booking.getFrom(), booking.getTo(), booking.getBookedSeats());
                return true;
            } else {
                return false;
            }
        } catch (NoSuchSubRouteException e) {
            return false;
        }
    }

    public boolean removeBooking(Booking booking) {
        if (bookings.remove(booking)) {
            addAvailableSeats(booking.getFrom(), booking.getTo(), booking.getBookedSeats());
            return true;
        } else {
            return false;
        }
    }

    public boolean isDefined() {
        return driver != null;
    }

    public long getId() {
        return id;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public Collection<Booking> getBookings() {
        return Collections.unmodifiableCollection(bookings);
    }

    public Driver getDriver() {
        return driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    private void addAvailableSeats(Stop departureStop, Stop arrivalStop, int seats) {
        updateAvailableSeats(departureStop, arrivalStop, seats);
    }

    private void subtractAvailableSeats(Stop departureStop, Stop arrivalStop, int seats) {
        updateAvailableSeats(departureStop, arrivalStop, -seats);
    }

    private void updateAvailableSeats(Stop departureStop, Stop arrivalStop, int seats) {
        try {
            Route subRoute = timetable.getRoute().makeSubRoute(departureStop, arrivalStop);
            subRoute.getLegs().forEach(l -> availableSeats.put(l, availableSeats.get(l) + seats));
        } catch (NoSuchSubRouteException e) {
            // asserting no exception
        }
    }
}
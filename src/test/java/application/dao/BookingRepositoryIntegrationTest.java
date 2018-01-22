package application.dao;

import application.Main;
import application.domain.Booking;
import application.domain.Client;
import application.domain.Location;
import application.domain.Stop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Main.class})
public class BookingRepositoryIntegrationTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private StopRepository stopRepository;

    private Stop from;
    private Stop to;
    private Client client;
    private Location location;

    @BeforeEach
    public void setUp() throws Exception {
        bookingRepository.deleteAll();
        stopRepository.deleteAll();

        client = new Client("name", "phone");
        location = new Location("place");
        from = new Stop("name1", location);
        to = new Stop("name2", location);
    }

    @Test
    @Transactional
    public void shouldRepositorySaveBookingIntegrationTest() {
        Booking booking = new Booking(client, from, to, 2);
        stopRepository.save(from);
        stopRepository.save(to);

        bookingRepository.save(booking);
        Optional<Booking> foundBooking = bookingRepository.findById(booking.getId());

        assertAll(
                () -> assertTrue(foundBooking.isPresent()),
                () -> assertEquals(booking, foundBooking.get())
        );
    }

    @Test
    @Transactional
    public void shouldRepositoryDeleteClientAndBookingIntegrationTest() {
        Booking booking = new Booking(client, from, to, 2);
        stopRepository.save(from);
        stopRepository.save(to);

        bookingRepository.save(booking);
        bookingRepository.delete(booking);

        assertAll(
                () -> assertFalse(bookingRepository.existsById(booking.getId()))
        );
    }

}
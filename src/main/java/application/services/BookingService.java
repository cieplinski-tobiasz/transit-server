package application.services;

import application.dao.BookingRepository;
import application.dao.TravelRepository;
import application.domain.Booking;
import application.domain.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BookingService {
    private BookingRepository bookingRepository;
    private TravelRepository travelRepository;

    @Autowired
    public BookingService(@Autowired BookingRepository bookingRepository, @Autowired TravelRepository travelRepository) {
        this.bookingRepository = bookingRepository;
        this.travelRepository = travelRepository;
    }

    public Set<Booking> getBookingsByTravelId(Long id) {
        Optional<Travel> travel = travelRepository.findById(id);

        if (travel.isPresent()) {
            return travel.get().getBookings();
        } else {
            return Collections.emptySet();
        }
    }

    public boolean addBookingToTravelById(Booking booking, Long travelId) {
        Optional<Travel> travel = travelRepository.findById(travelId);

        if (travel.isPresent() && travel.get().addBooking(booking)) {
            bookingRepository.save(booking);
            travelRepository.save(travel.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean removeBookingByIdFromTravelById(Long bookingId, Long travelId) {
        Optional<Travel> travel = travelRepository.findById(travelId);
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (travel.isPresent() && booking.isPresent() && travel.get().removeBooking(booking.get())) {
            travelRepository.save(travel.get());
            bookingRepository.delete(booking.get());
            return true;
        } else {
            return false;
        }
    }
}

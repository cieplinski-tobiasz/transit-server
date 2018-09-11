package application.services;

import application.repositories.BookingRepository;
import application.repositories.TravelRepository;
import application.domain.Booking;
import application.domain.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TravelRepository travelRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, TravelRepository travelRepository) {
        this.bookingRepository = bookingRepository;
        this.travelRepository = travelRepository;
    }

    public Set<Booking> getBookingsByTravelId(final long id) {
        return travelRepository.findById(id)
                .map(Travel::getBookings)
                .orElse(Collections.emptySet());
    }

    @Transactional
    public boolean addBookingToTravelById(final Booking booking, final long travelId) {
        Optional<Travel> travel = travelRepository.findById(travelId);

        if (travel.isPresent() && travel.get().addBooking(booking)) {
            bookingRepository.save(booking);
            travelRepository.save(travel.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean removeBookingFromTravelById(final Booking booking, final long travelId) {
        Optional<Travel> travel = travelRepository.findById(travelId);

        if (travel.isPresent() && travel.get().removeBooking(booking)) {
            travelRepository.save(travel.get());
            bookingRepository.delete(booking);
            return true;
        } else {
            return false;
        }
    }
}

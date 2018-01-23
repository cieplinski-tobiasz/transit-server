package application.rest.controllers;

import application.domain.Booking;
import application.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/bookmarks")
public class BookingRestController {
    private final BookingService bookingService;

    @Autowired
    public BookingRestController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{travelId}")
    Collection<Booking> readBookingsForTravelId(@PathVariable long travelId) {
        return bookingService.getBookingsByTravelId(travelId);
    }

    @RequestMapping(method = RequestMethod.DELETE, params = {"bookingId", "travelId" })
    ResponseEntity<?> removeBookingByIdFromTravelById(@RequestParam Long bookingId, @RequestParam Long travelId) {
        if (bookingService.removeBookingByIdFromTravelById(bookingId, travelId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = {"travelId"})
    ResponseEntity<?> addBookingToTravelById(@RequestParam Long travelId, @RequestBody Booking booking) {
        if (bookingService.addBookingToTravelById(booking, travelId)) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(booking.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

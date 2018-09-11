package application.services

import application.domain.Booking
import application.domain.Travel
import application.repositories.BookingRepository
import application.repositories.TravelRepository
import spock.lang.Specification

class BookingServiceSpec extends Specification {
    def bookingRepository = Mock(BookingRepository)
    def travelRepository = Mock(TravelRepository)
    def uut = new BookingService(bookingRepository, travelRepository)

    def "getBookingsByTravelId should call travelRepository in order to find travel"() {
        when:
        uut.getBookingsByTravelId(1)

        then:
        1 * travelRepository.findById(_) >> Optional.empty()
    }

    def "getBookingsByTravelId should return empty set when there is no travel with given id"() {
        given:
        travelRepository.findById(_) >> Optional.empty()

        expect:
        uut.getBookingsByTravelId(1).empty
    }

    def "getBookingsByTravelId should return empty set when travel does not contain bookings"() {
        given:
        def travel = Mock(Travel)
        travel.getBookings() >> []
        travelRepository.findById(_) >> Optional.of(travel)

        expect:
        uut.getBookingsByTravelId(1).empty
    }

    def "getBookingsByTravelId should return not empty set when travel contains bookings"() {
        given:
        def travel = Mock(Travel)
        travel.getBookings() >> [Mock(Booking)]
        travelRepository.findById(_) >> Optional.of(travel)

        expect:
        !uut.getBookingsByTravelId(1).empty
    }

    def "addBookingToTravelById should return false if travel with given id does not exist"() {
        given:
        travelRepository.findById(_) >> Optional.empty()

        expect:
        !uut.addBookingToTravelById(Mock(Booking), 1)
    }

    def "addBookingToTravelById should load travel from travelRepository"() {
        given:
        1 * travelRepository.findById(_) >> Optional.empty()

        expect:
        !uut.addBookingToTravelById(Mock(Booking), 1)
    }

    def "addBookingToTravelById should return false if booking cannot be added to travel"() {
        given:
        def travel = Mock(Travel)
        travel.addBooking(_) >> false
        travelRepository.findById(_) >> Optional.of(travel)

        expect:
        !uut.addBookingToTravelById(Mock(Booking), 1)
    }

    def "removeBookingFromTravelById should load the travel from repository and return false if it does not exist"() {
        given:
        1 * travelRepository.findById(_) >> Optional.empty()

        expect:
        !uut.removeBookingFromTravelById(Mock(Booking), 1)
    }

    def "removeBookingFromTravelById should return false if there is no such booking in travel"() {
        given:
        def travel = Mock(Travel)
        1 * travel.removeBooking(_) >> false
        1 * travelRepository.findById(_) >> Optional.of(travel)

        expect:
        !uut.removeBookingFromTravelById(Mock(Booking), 1)
    }

    def "removeBookingFromTravelById should save travel and delete booking if successful"() {
        given:
        def travel = Mock(Travel)
        def booking = Mock(Booking)
        travel.removeBooking(_) >> true
        travelRepository.findById(1) >> Optional.of(travel)
        1 * bookingRepository.delete(booking)
        1 * travelRepository.save(travel)

        expect:
        uut.removeBookingFromTravelById(booking, 1)
    }


    def "removeBookingFromTravelById should return true on success"() {
        given:
        def travel = Mock(Travel)
        travel.removeBooking(_) >> true
        travelRepository.findById(_) >> Optional.of(travel)

        expect:
        uut.removeBookingFromTravelById(Mock(Booking), 1)
    }
}

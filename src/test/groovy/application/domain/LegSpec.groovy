package application.domain

import spock.lang.Specification

class LegSpec extends Specification {
    def "isDepartingFrom should return true if it is leg's departure stop"() {
        given:
        def departureStop = Mock(Stop)
        def uut = new Leg(departureStop, Mock(Stop), null)

        expect:
        uut.isDepartingFrom(departureStop)
    }

    def "isDepartingFrom should return false if it is not leg's departure stop"() {
        given:
        def departureStop = Mock(Stop)
        def notDepartureStop = Mock(Stop)
        def uut = new Leg(departureStop, Mock(Stop), null)

        expect:
        !uut.isDepartingFrom(notDepartureStop)
    }

    def "isArrivingAt should return true if it is leg's arrival stop"() {
        given:
        def arrivalStop = Mock(Stop)
        def uut = new Leg(Mock(Stop), arrivalStop, null)

        expect:
        uut.isArrivingAt(arrivalStop)
    }

    def "isArrivingAt should return false if it is not leg's arrival stop"() {
        given:
        def arrivalStop = Mock(Stop)
        def notArrivalStop = Mock(Stop)
        def uut = new Leg(Mock(Stop), arrivalStop, null)

        expect:
        !uut.isArrivingAt(notArrivalStop)
    }
}

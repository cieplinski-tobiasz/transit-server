package application.domain

import spock.lang.Specification

class StopSpec extends Specification {
    def "getDescription should return an empty optional given description is null"() {
        given:
            def uut = new Stop("test name", Mock(Location))

        expect:
        !uut.description.isPresent()
    }

    def "getDescription should return an optional with description given description is not null"() {
        given:
        def uut = new Stop("test name", "description", Mock(Location))

        when:
        def result = uut.description

        then:
        result.isPresent()
        result.get() == "description"
    }
}

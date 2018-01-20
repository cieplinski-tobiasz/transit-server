package domain;

import exceptions.NoSuchSubRouteException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Route {
    private final List<Leg> legs;

    public Route(List<Leg> legs) {
        this.legs = legs;
    }

    public Optional<Leg> getLegForDepartureStop(Stop stop) {
        return legs.stream()
                .filter(l -> l.getDepartureStop().equals(stop))
                .findFirst();
    }

    public Optional<Leg> getLegForArrivalStop(Stop stop) {
        return legs.stream()
                .filter(l -> l.getArrivalStop().equals(stop))
                .findFirst();
    }

    public Route makeSubRoute(Stop departure, Stop arrival) throws NoSuchSubRouteException {
        int departureIndex = legs.indexOf(getLegForDepartureStop(departure)
                .orElseThrow(NoSuchSubRouteException::new));

        int arrivalIndex = legs.indexOf(getLegForArrivalStop(arrival)
                .orElseThrow(NoSuchSubRouteException::new));

        if (departureIndex == -1 || arrivalIndex == -1 || departureIndex > arrivalIndex) {
            throw new NoSuchSubRouteException();
        }

        return new Route(legs.subList(departureIndex, arrivalIndex + 1));
    }

    public List<Leg> getLegs() {
        return Collections.unmodifiableList(legs);
    }
}

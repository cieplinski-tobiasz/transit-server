package application.domain;

import application.exceptions.NoSuchSubRoute;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Entity
@Immutable
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToMany(fetch = FetchType.EAGER)
    @NotNull
    private final List<Leg> legs;

    private Route() {
        this.legs = null;
    }

    public Route(List<Leg> legs) {
        this.legs = legs;
    }

    public long getId() {
        return id;
    }

    public Optional<Leg> getLegForDepartureStop(Stop stop) {
        return legs.stream()
                .filter(leg -> leg.isDepartingFrom(stop))
                .findFirst();
    }

    public Optional<Leg> getLegForArrivalStop(Stop stop) {
        return legs.stream()
                .filter(leg -> leg.isArrivingAt(stop))
                .findFirst();
    }

    public Route makeSubRoute(Stop departure, Stop arrival) throws NoSuchSubRoute {
        int departureIndex = legs.indexOf(getLegForDepartureStop(departure)
                .orElseThrow(NoSuchSubRoute::new));

        int arrivalIndex = legs.indexOf(getLegForArrivalStop(arrival)
                .orElseThrow(NoSuchSubRoute::new));

        if (departureIndex == -1 || arrivalIndex == -1 || departureIndex > arrivalIndex) {
            throw new NoSuchSubRoute();
        }

        return new Route(legs.subList(departureIndex, arrivalIndex + 1));
    }

    public List<Leg> getLegs() {
        return legs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        return legs.equals(route.legs);
    }

    @Override
    public int hashCode() {
        return legs.hashCode();
    }
}

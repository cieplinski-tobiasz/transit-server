package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Entity
@Immutable
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @OneToOne
    private final Route route;

    @NotNull
    @ElementCollection
    private final Map<Leg, LocalTime> departureTimeMap;

    @NotNull
    @ElementCollection
    private final Map<Leg, LocalTime> arrivalTimeMap;

    private Timetable() {
        this.route = null;
        this.departureTimeMap = null;
        this.arrivalTimeMap = null;
    }

    public Timetable(Route route, Map<Leg, LocalTime> departureTimeMap, Map<Leg, LocalTime> arrivalTimeMap) {
        this.route = route;
        this.departureTimeMap = departureTimeMap;
        this.arrivalTimeMap = arrivalTimeMap;
    }

    public long getId() {
        return id;
    }

    public Optional<LocalTime> getDepartureTimeForStop(Stop stop) {
        return route.getLegForDepartureStop(stop)
                .map(departureTimeMap::get);
    }

    public Optional<LocalTime> getArrivalTimeForStop(Stop stop) {
        return route.getLegForArrivalStop(stop)
                .map(arrivalTimeMap::get);
    }

    public Optional<LocalTime> getDepartureTimeForLeg(Leg leg) { return Optional.ofNullable(departureTimeMap.get(leg)); }

    public Optional<LocalTime> getArrivalTimeForLeg(Leg leg) { return Optional.ofNullable(arrivalTimeMap.get(leg)); }

    public Route getRoute() {
        return route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timetable timetable = (Timetable) o;

        if (!route.equals(timetable.route)) return false;
        if (!departureTimeMap.equals(timetable.departureTimeMap)) return false;
        return arrivalTimeMap.equals(timetable.arrivalTimeMap);
    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + departureTimeMap.hashCode();
        result = 31 * result + arrivalTimeMap.hashCode();
        return result;
    }
}

package domain;

import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

public class Timetable {
    private final Route route;
    private final Map<Leg, LocalTime> departureTimeMap;
    private final Map<Leg, LocalTime> arrivalTimeMap;

    public Timetable(Route route, Map<Leg, LocalTime> departureTimeMap, Map<Leg, LocalTime> arrivalTimeMap) {
        this.route = route;
        this.departureTimeMap = departureTimeMap;
        this.arrivalTimeMap = arrivalTimeMap;
    }

    public Optional<LocalTime> getDepartureTimeForStop(Stop stop) {
        Optional<Leg> departureLeg = route.getLegForDepartureStop(stop);

        return departureLeg.isPresent() ?
                Optional.ofNullable(arrivalTimeMap.get(departureLeg.get())) :
                Optional.empty();
    }

    public Optional<LocalTime> getArrivalTimeForStop(Stop stop) {
        Optional<Leg> departureLeg = route.getLegForArrivalStop(stop);

        return departureLeg.isPresent() ?
                Optional.ofNullable(arrivalTimeMap.get(departureLeg.get())) :
                Optional.empty();
    }

    public Optional<LocalTime> getDepartureTimeForLeg(Leg leg) { return Optional.ofNullable(departureTimeMap.get(leg)); }

    public Optional<LocalTime> getArrivalTimeForLeg(Leg leg) { return Optional.ofNullable(arrivalTimeMap.get(leg)); }

    public Route getRoute() {
        return route;
    }
}

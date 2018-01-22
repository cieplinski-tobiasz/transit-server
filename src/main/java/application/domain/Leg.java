package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Immutable
public class Leg {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    @OneToOne
    private final Stop departureStop;
    @NotNull
    @OneToOne
    private final Stop arrivalStop;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final Distance distance;

    private Leg() {
        this.departureStop = null;
        this.arrivalStop = null;
        this.distance = null;
    }

    public Leg(Stop departureStop, Stop arrivalStop, Distance distance) {
        this.departureStop = departureStop;
        this.arrivalStop = arrivalStop;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public Stop getDepartureStop() {
        return departureStop;
    }

    public Stop getArrivalStop() {
        return arrivalStop;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leg leg = (Leg) o;

        if (!departureStop.equals(leg.departureStop)) return false;
        if (!arrivalStop.equals(leg.arrivalStop)) return false;
        return distance.equals(leg.distance);

    }

    @Override
    public int hashCode() {
        int result = departureStop.hashCode();
        result = 31 * result + arrivalStop.hashCode();
        result = 31 * result + distance.hashCode();
        return result;
    }
}

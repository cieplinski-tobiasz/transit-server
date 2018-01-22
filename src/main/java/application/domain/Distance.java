package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Immutable
public class Distance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private final double kilometers;

    private Distance() {
        this.kilometers = 0.0;
    }

    public Distance(double kilometers) {
        this.kilometers = kilometers;
    }

    public double getKilometers() {
        return kilometers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Distance distance = (Distance) o;

        return Double.compare(distance.kilometers, kilometers) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(kilometers);
        return (int) (temp ^ (temp >>> 32));
    }
}

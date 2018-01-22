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
}

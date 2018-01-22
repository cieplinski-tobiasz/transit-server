package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Immutable
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    private final String address;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final Coordinates coordinates;

    private Location() {
        this.address = null;
        this.coordinates = null;
    }

    public Location(String address, Coordinates coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }

    public Location(String address) {
        this(address, null);
    }

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Optional<Coordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }
}

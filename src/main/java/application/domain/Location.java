package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Embeddable
@Immutable
public class Location {
    @NotNull
    private final String address;

    @Embedded
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!address.equals(location.address)) return false;
        return coordinates != null ? coordinates.equals(location.coordinates) : location.coordinates == null;

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}

package domain;

import java.util.Optional;

public class Location {
    private final String address;
    private final Coordinates coordinates;

    public Location(String address, Coordinates coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }

    public Location(String address) {
        this(address, null);
    }

    public String getAddress() {
        return address;
    }

    public Optional<Coordinates> getCoordinates() {
        return Optional.ofNullable(coordinates);
    }
}

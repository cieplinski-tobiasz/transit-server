package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Entity
@Immutable
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @NotNull
    private final String name;
    private final String description;
    @NotNull
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private final Location location;

    private Stop() {
        this.name = null;
        this.description = null;
        this.location = null;
    }

    public Stop(String name, String description, Location location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public Stop(String name, Location location) {
        this(name, null, location);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        if (!name.equals(stop.name)) return false;
        if (description != null ? !description.equals(stop.description) : stop.description != null) return false;
        return location.equals(stop.location);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + location.hashCode();
        return result;
    }
}

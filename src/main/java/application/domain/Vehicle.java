package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Immutable
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    private String name;

    private int capacity;

    private Vehicle() {
    }

    public Vehicle(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (capacity != vehicle.capacity) return false;
        return name.equals(vehicle.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + capacity;
        return result;
    }
}

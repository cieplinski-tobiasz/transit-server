package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Immutable
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    private final String name;

    @NotNull
    private final String phoneNumber;

    private Driver() {
        this.name = null;
        this.phoneNumber = null;
    }

    public Driver(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        if (!name.equals(driver.name)) return false;
        return phoneNumber.equals(driver.phoneNumber);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        return result;
    }
}

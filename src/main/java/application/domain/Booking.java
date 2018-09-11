package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Immutable
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final Client client;

    @NotNull
    @OneToOne
    private final Stop from;

    @NotNull
    @OneToOne
    private final Stop to;

    private final int bookedSeats;

    private Booking() {
        this.client = null;
        this.from = null;
        this.to = null;
        this.bookedSeats = 0;
    }

    public Booking(Client client, Stop from, Stop to, int seats) {
        this.client = client;
        this.from = from;
        this.to = to;
        this.bookedSeats = seats;
    }

    public long getId() {
        return id;
    }

    public Stop getFrom() {
        return from;
    }

    public Stop getTo() {
        return to;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (bookedSeats != booking.bookedSeats) return false;
        if (!client.equals(booking.client)) return false;
        if (!from.equals(booking.from)) return false;
        return to.equals(booking.to);
    }

    @Override
    public int hashCode() {
        int result = client.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + bookedSeats;
        return result;
    }
}

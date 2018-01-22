package application.domain;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Immutable
@Table(name = "BOOKING")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @NotNull
    private final Client client;
    @NotNull
    @OneToOne
    private final Stop from;
    @NotNull
    @OneToOne
    private final Stop to;
    @NotNull
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

    public Client getClient() {
        return client;
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
}

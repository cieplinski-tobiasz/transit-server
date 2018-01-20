package domain;

public class Booking {
    private final Client client;
    private final Stop from;
    private final Stop to;
    private final int bookedSeats;

    public Booking(Client client, Stop from, Stop to, int seats) {
        this.client = client;
        this.from = from;
        this.to = to;
        this.bookedSeats = seats;
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

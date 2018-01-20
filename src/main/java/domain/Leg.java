package domain;

public class Leg {
    private final Stop departureStop;
    private final Stop arrivalStop;
    private final Distance distance;

    public Leg(Stop departureStop, Stop arrivalStop, Distance distance) {
        this.departureStop = departureStop;
        this.arrivalStop = arrivalStop;
        this.distance = distance;
    }

    public Stop getDepartureStop() {
        return departureStop;
    }

    public Stop getArrivalStop() {
        return arrivalStop;
    }

    public Distance getDistance() {
        return distance;
    }
}

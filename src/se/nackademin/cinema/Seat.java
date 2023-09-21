package se.nackademin.cinema;

public class Seat {

    private final String seatNumber;
    private boolean isBooked;

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
        this.isBooked = false;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void reserveSeat(boolean booked) {
        if (this.isBooked == false && booked == true) {
            this.isBooked = true;
        }
        isBooked = booked;
    }
}

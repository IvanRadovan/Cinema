package se.nackademin.cinema;

import java.util.Objects;

public final class Seat implements Comparable<Seat> {

    private final String number;
    private final double price;
    private boolean isBooked;

    public Seat(String number, double price) {
        if (number == null)
            throw new NullPointerException("Illegal argument passed: Seat number cannot be null.");
        if (price <= 0)
            throw new IllegalArgumentException("Illegal argument passed: Price most be higher than 0 SEK.");

        this.number = number.toUpperCase();
        this.price = price;
        this.isBooked = false;
    }

    public String getNumber() {
        return (this.number != null) ? number.toUpperCase() : "N/A";
    }

    public double getPrice() {
        return (this.price > 0) ? price : -1;
    }

    public boolean isBooked() {
        return isBooked;
    }


    public void bookSeat(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return number + " = " + price + " SEK";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat seat)) return false;
        return Double.compare(price, seat.price) == 0
                && isBooked == seat.isBooked
                && Objects.equals(number, seat.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, price, isBooked);
    }

    @Override
    public int compareTo(Seat o) {
        return this.number.compareTo(o.number);
    }
}

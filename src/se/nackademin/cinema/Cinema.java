package se.nackademin.cinema;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class Cinema {

    private static final int SEAT_PRICE = 150;
    private static final int COLUMN_SIZE = 8;
    private static final int ROW_SIZE = 8;

    private final Set<Seat> seats;
    Movie movie;

    Cinema(Movie movie) {
        IntFunction<Seat> seatNameGenerator = i -> {
            char row = (char) ('A' + i / COLUMN_SIZE);
            int column = i % COLUMN_SIZE + 1;
            String seatNumber = String.format("%s%02d", row, column);
            return new Seat(seatNumber, SEAT_PRICE);
        };

        seats = IntStream.range(0, ROW_SIZE * COLUMN_SIZE)
                .mapToObj(seatNameGenerator)
                .collect(Collectors.toCollection(TreeSet::new));

        DataHandler.getInstance(movie).loadBookedSeats(movie);
    }

    Seat getSeat(String number) {
        return seats.stream()
                .filter(seat -> seat.getNumber().equalsIgnoreCase(number))
                .findFirst()
                .orElse(null);
    }

    boolean bookSeat(Seat seat) {
        if (seat != null && seats.contains(seat)) {
            if (!seat.isBooked()) {
                seat.bookSeat(true);
                DataHandler.getInstance(movie).saveBookedSeat(movie.getTitle(), seat);
                return true;
            } else
                System.out.println("Seat is already taken.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    boolean changeSeat(Seat bookedSeat, Seat newSeat, String ticket) {
        if (bookedSeat == null || newSeat == null || !seats.contains(bookedSeat) || !seats.contains(newSeat)) {
            System.out.println("Invalid seats provided.");
            return false;
        }

        if (!bookedSeat.isBooked()) {
            System.out.println("You need a booked ticket in order to change the seats.");
            return false;
        }

        if (newSeat.isBooked()) {
            System.out.println("The new seat is not available.");
            return false;
        }


        if (DataHandler.getInstance(movie).changeTicket(ticket, bookedSeat, newSeat)) {
            bookedSeat.bookSeat(false);
            DataHandler.getInstance(movie).removeBookedSeat(movie.getTitle(), bookedSeat);
            return bookSeat(newSeat);
        }

        return false;
    }

    boolean cancelSeat(String movie, Seat seat) {
        if (seat != null && seats.contains(seat)) {
            if (seat.isBooked()) {
                seat.bookSeat(false);
                DataHandler.getInstance(this.movie).removeBookedSeat(movie, seat);
                return true;
            } else
                System.out.println("Seat is available.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    void printSeatsTable() {
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String WHITE = "\u001B[0m";

        final String MESSAGE = "Seats Available:";
        final String FREE = "%s[%s]";
        final String TAKEN = "%s[ X ]";

        System.out.println(MESSAGE);
        seats.forEach(seat -> {
            Seat currentSeat = getSeat(seat.getNumber());
            if (currentSeat != null) {
                String seatCell = (!currentSeat.isBooked()) ? FREE.formatted(GREEN, seat.getNumber()) : TAKEN.formatted(RED);
                System.out.print(seatCell);
                System.out.print(WHITE); // Reset color to default
                if (currentSeat.getNumber().endsWith(String.valueOf(COLUMN_SIZE)))
                    System.out.println();
            }
        });
    }


}

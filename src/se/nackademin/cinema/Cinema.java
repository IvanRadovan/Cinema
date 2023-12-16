package se.nackademin.cinema;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class Cinema {

    private static final int SEAT_PRICE = 150;
    private static final int COLUMN_SIZE = 8;
    private static final int ROW_SIZE = 8;

    private final String movie;
    private final Set<Seat> seats;
    DataHandler dataHandler;

    Cinema(String movie) {
        this.movie = movie;
        dataHandler = new DataHandler(this);

        IntFunction<Seat> seatNameGenerator = i -> {
            char row = (char) ('A' + i / COLUMN_SIZE);
            int column = i % COLUMN_SIZE + 1;
            String seatNumber = String.format("%s%02d", row, column);
            return new Seat(seatNumber, SEAT_PRICE);
        };

        seats = IntStream.range(0, ROW_SIZE * COLUMN_SIZE)
                .mapToObj(seatNameGenerator)
                .collect(Collectors.toCollection(TreeSet::new));

        dataHandler.loadBookedSeats();
    }

    String getMovie() {
        return (this.movie != null) ? this.movie.toUpperCase() : "N/A";
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
                dataHandler.saveBookedSeat(seat);
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

        if (!Files.exists(Paths.get(DataHandler.TICKETS_DIRECTORY + ticket))) {
            System.out.println("Ticket not found.");
            return false;
        }


        if (dataHandler.changeTicket(ticket, bookedSeat, newSeat)) {
            bookedSeat.bookSeat(false);
            dataHandler.removeSavedBookedSeat(bookedSeat);
            return bookSeat(newSeat);
        }

        return false;
    }

    boolean cancelSeat(Seat seat) {
        if (seat != null && seats.contains(seat)) {
            if (seat.isBooked()) {
                seat.bookSeat(false);
                dataHandler.removeSavedBookedSeat(seat);
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

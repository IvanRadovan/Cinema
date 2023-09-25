package se.nackademin.cinema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class Cinema {

    private static final int SEAT_PRICE = 150;
    private static final int COLUMN_SIZE = 8;
    private static final int ROW_SIZE = 8;
    private static final String BOOKED_SEATS_FILE = "dataFiles\\bookedSeats\\bookedSeats.txt";
    private static final String TICKETS_DIRECTORY = "dataFiles\\tickets\\";

    private final String movie;
    private final Set<Seat> seats;

    Cinema(String movie) {
        this.movie = movie;

        IntFunction<Seat> seatNameGenerator = i -> {
            char row = (char) ('A' + i / COLUMN_SIZE);
            int column = i % COLUMN_SIZE + 1;
            String seatNumber = String.format("%s%02d", row, column);
            return new Seat(seatNumber, SEAT_PRICE);
        };

        seats = IntStream.range(0, ROW_SIZE * COLUMN_SIZE)
                .mapToObj(seatNameGenerator)
                .collect(Collectors.toCollection(TreeSet::new));

        loadBookedSeats();
    }

    String getMovie() {
        return (this.movie != null) ? this.movie.toUpperCase() : "N/A";
    }

    Seat getSeat(String number) {
        for (Seat seat : seats) {
            if (seat.getNumber().equals(number))
                return seat;
        }
        return null;
    }

    boolean bookSeat(Seat seat) {
        if (seats.contains(seat)) {
            if (!seat.isBooked()) {
                seat.bookSeat(true);
                saveBookedSeat(seat);
                return true;
            } else
                System.out.println("Seat is already taken.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    boolean changeSeat(Seat bookedSeat, Seat newSeat) {
        if (seats.contains(bookedSeat)) {
            if (bookedSeat.isBooked()) {
                if (!newSeat.isBooked()) {
                    bookedSeat.bookSeat(false);
                    removeSavedBookedSeat(bookedSeat);
                    return bookSeat(newSeat);
                } else
                    System.out.println("Seat is not available");
            } else
                System.out.println("Your seat has to be booked.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    boolean cancelSeat(Seat seat) {
        if (seats.contains(seat)) {
            if (seat.isBooked()) {
                seat.bookSeat(false);
                removeSavedBookedSeat(seat);
                return true;
            } else
                System.out.println("Seat is available.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    void printSeatsTable() {
        System.out.println("Seats Available:");
        seats.forEach(seat -> {
            Seat currentSeat = getSeat(seat.getNumber());
            if (currentSeat != null) {
                String seatCell = (!currentSeat.isBooked()) ? "[" + seat.getNumber() + "] " : "[ X ] ";
                System.out.print(seatCell);
                if (currentSeat.getNumber().endsWith(String.valueOf(COLUMN_SIZE)))
                    System.out.println();
            }
        });
    }

    void getTicket(List<String> fileData) {
        String fileName = setPersonalizedTicketFileName(fileData);
        Path filePath = Path.of(TICKETS_DIRECTORY + fileName);
        String title = this.movie.toUpperCase() + "\n";
        try {
            Files.write(filePath, title.getBytes(), StandardOpenOption.CREATE);
            Files.write(filePath, fileData, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error while printing ticket: " + e.getMessage());
        }
    }

    private void saveBookedSeat(Seat seat) {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            String seatInfo = "Seat: " + seat.getNumber() + "\n";
            Files.write(filePath, seatInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error while saving booked seat: " + e.getMessage());
        }
    }

    private void loadBookedSeats() {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            List<String> fileLinesList = Files.readAllLines(filePath);
            fileLinesList.forEach(line -> {
                String seatNumber = line.substring(line.indexOf(" ") + 1);
                Seat seat = getSeat(seatNumber);
                if (seat != null)
                    seat.bookSeat(true);
            });
        } catch (IOException e) {
            System.out.println("Error while loading booked seats: " + e.getMessage());
        }
    }

    private void removeSavedBookedSeat(Seat seat) {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            List<String> fileLinesList = Files.readAllLines(filePath);
            fileLinesList.removeIf(i -> i.endsWith(" " + seat.getNumber()));
            Files.write(filePath, fileLinesList);
        } catch (IOException e) {
            System.out.println("Error while overwriting booked seats: " + e.getMessage());
        }
    }

    private String setPersonalizedTicketFileName(List<String> fileData) {
        int nameDataIndex = 0;
        int seatDataIndex = 3;
        String whitespace = " ";
        String underscore = "_";
        String txtExtension = ".txt";

        String nameData = fileData.get(nameDataIndex);
        String retrieveFullName = nameData.substring(nameData.indexOf(whitespace) + 1);
        String seatNumberData = fileData.get(seatDataIndex);
        String retrieveSeatNumber = seatNumberData.substring(seatNumberData.indexOf(whitespace) + 1);

        return retrieveFullName.replace(whitespace, underscore).concat(underscore).concat(retrieveSeatNumber).concat(txtExtension).toLowerCase();
    }
}

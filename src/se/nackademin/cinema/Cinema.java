package se.nackademin.cinema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cinema {

    private static final int SEAT_PRICE = 150;
    private static final int COLUMN_SIZE = 8;
    private static final int ROW_SIZE = 8;
    private static final String BOOKED_SEATS_FILE = "dataFiles\\bookedSeats\\bookedSeats.txt";
    private static final String TICKETS_DIRECTORY = "dataFiles\\tickets\\";

    private final String movieTitle;
    private final Set<Seat> seats;

    public Cinema(String movieTitle) {
        this.movieTitle = movieTitle;

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
                saveBookedSeat(seat);
                return true;
            } else
                System.out.println("Seat is already taken.");
        } else
            System.out.println("Seat does not exist.");
        return false;
    }

    boolean changeSeat(Seat bookedSeat, Seat newSeat) {
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


//        if (changeTicket(ticket, bookedSeat, newSeat)) {
//            bookedSeat.bookSeat(false);
//            removeBookedSeat(bookedSeat);
//            return bookSeat(newSeat);
//        }

        return false;
    }

    boolean cancelSeat(Seat seat) {
        if (seat != null && seats.contains(seat)) {
            if (seat.isBooked()) {
                seat.bookSeat(false);
                removeBookedSeat(seat);
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

    void printTicket(List<String> fileData) {
        String fileName = generatePersonalizedTicket(fileData);
        final Path filePath = Paths.get(TICKETS_DIRECTORY + fileName);

        try (var writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE)) {
            for (String line : fileData) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while printing ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadBookedSeats() {
        try (var reader = Files.newBufferedReader(Paths.get(BOOKED_SEATS_FILE))) {
            reader.lines()
                    .filter(line -> line.startsWith(movieTitle))
                    .findFirst()
                    .ifPresent(bookedSeats -> Arrays.stream(bookedSeats.split(" "))
                            .forEach(seatNumber -> {
                                Seat currentSeat = getSeat(seatNumber);
                                if (currentSeat != null)
                                    getSeat(seatNumber).bookSeat(true);
                            }));
        } catch (IOException e) {
            System.out.println("Error while loading booked seat: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void saveBookedSeat(Seat seat) {
        Path path = Paths.get(BOOKED_SEATS_FILE);
        try (BufferedReader reader = Files.newBufferedReader(path);
             BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            List<String> lines = reader.lines().collect(Collectors.toList());
            boolean movieExists = lines.stream().anyMatch(line -> line.startsWith(movieTitle));

            if (movieExists) {
                lines = lines.stream()
                        .filter(line -> line.startsWith(movieTitle))
                        .map(line -> line.concat("%S ".formatted(seat)))
                        .toList();
            } else
                lines.add("%s %S ".formatted(movieTitle, seat.getNumber()));

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving booked seat: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean changeTicket(String ticket, Seat bookedSeat, Seat newSeat) {
        final Path PATH = Paths.get(TICKETS_DIRECTORY + ticket);
        final String DATE_TIME = "Date & Time: ";
        final String SEAT = "Seat: ";

        if (!Files.exists(PATH)) {
            System.out.println("No ticket was found.");
            return false;
        }

        try {
            var lines = Files.newBufferedReader(PATH).lines().toList();

            if (lines.stream().noneMatch(line -> line.contains(bookedSeat.getNumber()))) {
                System.out.println("Wrong seat according to the ticket.");
                return false;
            }

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(SEAT))
                    lines.set(i, SEAT.concat(newSeat.getNumber()));
//                else if (line.startsWith(DATE_TIME))
//                    lines.set(i, DATE_TIME.concat(getDateAndTime()));
            }

            Files.delete(PATH);
            printTicket(lines);
            removeBookedSeat(bookedSeat);
            saveBookedSeat(newSeat);

            return true;
        } catch (IOException e) {
            System.out.println("Error while updating ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void removeBookedSeat(Seat seat) {
        final Path PATH = Paths.get(BOOKED_SEATS_FILE);
        try (var reader = Files.newBufferedReader(PATH);
             var writer = Files.newBufferedWriter(PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            List<String> bookedSeatData = reader.lines()
                    .filter(line -> line.startsWith(movieTitle))
                    .map(line -> line.replace(seat.getNumber(), ""))
                    .toList();

            for (String line : bookedSeatData) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while updating the booked seat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String generatePersonalizedTicket(List<String> fileData) {
        final int NAME_INDEX = 1;
        final int SEAT_INDEX = 4;
        final String WHITESPACE = " ";
        final String UNDERSCORE = "_";
        final String TXT_EXTENSION = ".txt";

        final String NAME = fileData.get(NAME_INDEX);
        final String FETCH_NAME = NAME.substring(NAME.indexOf(WHITESPACE) + 1);
        final String SEAT = fileData.get(SEAT_INDEX);
        final String FETCH_SEAT = SEAT.substring(SEAT.indexOf(WHITESPACE) + 1);


        return FETCH_NAME.replace(WHITESPACE, UNDERSCORE).concat(UNDERSCORE).concat(FETCH_SEAT).concat(TXT_EXTENSION).toLowerCase();
    }
}

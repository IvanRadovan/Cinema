package se.nackademin.cinema;

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

    private static final int COLUMN_SIZE = 8;
    private static final int ROW_SIZE = 8;
    private static final String BOOKED_SEATS_FILE = "dataFiles\\bookedSeats\\bookedSeats.txt";
    private static final String TICKETS_DIRECTORY = "dataFiles\\tickets\\";

    private final String movieTitle;
    private final Set<Seat> seats;
    private final FileHandler fileHandler;

    public Cinema(String movieTitle, double ticketPrice) {
        this.fileHandler = new TextFileHandler();
        this.movieTitle = movieTitle;

        IntFunction<Seat> seatNameGenerator = i -> {
            char row = (char) ('A' + i / COLUMN_SIZE);
            int column = i % COLUMN_SIZE + 1;
            String seatNumber = String.format("%s%02d", row, column);
            return new Seat(seatNumber, ticketPrice);
        };

        seats = IntStream.range(0, ROW_SIZE * COLUMN_SIZE)
                .mapToObj(seatNameGenerator)
                .collect(Collectors.toCollection(TreeSet::new));

        loadBookedSeats();
    }

    public Seat getSeat(String number) {
        return seats.stream()
                .filter(seat -> seat.getNumber().equalsIgnoreCase(number))
                .findFirst()
                .orElse(null);
    }

    public boolean bookSeat(Seat seat) {
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

    public boolean cancelSeat(Seat seat) {
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

    public void printSeatsTable() {
        final String MESSAGE = "Seats Available:";
        final String FREE = "%s[%s]";
        final String TAKEN = "%s[ X ]";

        System.out.println(MESSAGE);
        seats.forEach(seat -> {
            Seat currentSeat = getSeat(seat.getNumber());
            if (currentSeat != null) {
                String seatCell = (!currentSeat.isBooked()) ? FREE.formatted(ANSI.GREEN, seat.getNumber()) : TAKEN.formatted(ANSI.RED);
                System.out.print(seatCell);
                System.out.print(ANSI.RESET);
                if (currentSeat.getNumber().endsWith(String.valueOf(COLUMN_SIZE)))
                    System.out.println();
            }
        });
    }

    public void printTicket(List<String> fileData) {
        String fileName = generatePersonalizedTicket(fileData);
        final Path PATH = Paths.get(TICKETS_DIRECTORY + fileName);
        fileHandler.save(PATH, fileData, StandardOpenOption.CREATE);
    }

    private void loadBookedSeats() {
        fileHandler.load(Paths.get(BOOKED_SEATS_FILE))
                .stream()
                .filter(line -> line.startsWith(movieTitle))
                .findFirst()
                .ifPresent(bookedSeats -> Arrays.stream(bookedSeats.split(" "))
                        .forEach(seatNumber -> {
                            Seat currentSeat = getSeat(seatNumber);
                            if (currentSeat != null)
                                getSeat(seatNumber).bookSeat(true);
                        }));
    }

    private void saveBookedSeat(Seat seat) {
        final Path path = Paths.get(BOOKED_SEATS_FILE);
        List<String> lines = fileHandler.load(path);
        boolean movieExists = lines.stream().anyMatch(line -> line.startsWith(movieTitle));

        if (movieExists) {
            lines = lines.stream()
                    .map(line -> line.startsWith(movieTitle)
                            ? line + seat.getNumber().concat(" ")
                            : line)
                    .toList();

        } else
            lines.add("%s %S ".formatted(movieTitle, seat.getNumber()));

        fileHandler.save(path, lines, StandardOpenOption.CREATE);
    }

    private void removeBookedSeat(Seat seat) {
        final Path path = Paths.get(BOOKED_SEATS_FILE);
        List<String> lines = fileHandler.load(path);

        lines = lines.stream()
                .map(line -> line.startsWith(movieTitle)
                        ? line.replace(seat.getNumber(), "").trim().concat(" ")
                        : line)
                .toList();

        fileHandler.save(path, lines, StandardOpenOption.CREATE);
    }

    private String generatePersonalizedTicket(List<String> fileData) {
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

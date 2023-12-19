package se.nackademin.cinema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataHandler {

    private static final String BOOKED_SEATS_FILE = "dataFiles\\bookedSeats\\bookedSeats.txt";
    private static final String TICKETS_DIRECTORY = "dataFiles\\tickets\\";

    private static volatile DataHandler instance;
    private final Movie movie;

    private DataHandler(Movie movie) {
        this.movie = movie;
    }

    public static DataHandler getInstance(Movie data) {
        DataHandler result = instance;
        if (result == null) {
            synchronized (DataHandler.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DataHandler(data);
                }
            }
        }
        return result;
    }


    public void printTicket(List<String> fileData) {
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

    public boolean changeTicket(String ticket, Seat bookedSeat, Seat newSeat) {
        final Path PATH = Paths.get(TICKETS_DIRECTORY + ticket);
        final String MOVIE = "Movie: ";
        final String DATE_TIME = "Date & Time: ";
        final String SEAT = "Seat: ";
        String MOVIE_TITLE = "";

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
                if (line.startsWith(MOVIE)) 
                    MOVIE_TITLE = line.substring(line.lastIndexOf(" ") + 1);
                if (line.startsWith(SEAT)) 
                    lines.set(i, SEAT.concat(newSeat.getNumber()));
                else if (line.startsWith(DATE_TIME)) 
                    lines.set(i, DATE_TIME.concat(getDateAndTime()));
            }

            Files.delete(PATH);
            printTicket(lines);
            removeBookedSeat(MOVIE_TITLE, bookedSeat);
            saveBookedSeat(MOVIE_TITLE, newSeat);

            return true;
        } catch (IOException e) {
            System.out.println("Error while updating ticket: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void saveBookedSeat(String movie, Seat seat) {
        Path path = Paths.get(BOOKED_SEATS_FILE);
        try (BufferedReader reader = Files.newBufferedReader(path);
             BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            List<String> lines = reader.lines().collect(Collectors.toList());
            boolean movieExists = lines.stream().anyMatch(line -> line.startsWith(movie));

            if (movieExists) {
                lines = lines.stream()
                        .filter(line -> line.startsWith(movie))
                        .map(line -> line.concat("%S ".formatted(seat)))
                        .toList();
            } else
                lines.add("%s %S ".formatted(movie, seat.getNumber()));

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving booked seat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadBookedSeats(Movie movie) {
        try (var reader = Files.newBufferedReader(Paths.get(BOOKED_SEATS_FILE))) {
            reader.lines()
                    .filter(line -> line.startsWith(movie.getTitle()))
                    .findFirst()
                    .ifPresent(bookedSeats -> Arrays.stream(bookedSeats.split(" "))
                            .forEach(seatNumber -> movie.getCinema().getSeat(seatNumber).bookSeat(true)));
        } catch (IOException e) {
            System.out.println("Error while loading booked seat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeBookedSeat(String movie, Seat seat) {
        final Path PATH = Paths.get(BOOKED_SEATS_FILE);
        try (var reader = Files.newBufferedReader(PATH);
             var writer = Files.newBufferedWriter(PATH, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            List<String> bookedSeatData = reader.lines()
                    .filter(line -> line.startsWith(movie))
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

    public static String getDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        return "Date & Time: " + formattedDateTime;
    }
}

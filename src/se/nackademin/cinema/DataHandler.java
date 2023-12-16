package se.nackademin.cinema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DataHandler {

    protected static final String BOOKED_SEATS_FILE = "dataFiles\\bookedSeats\\bookedSeats.txt";
    protected static final String TICKETS_DIRECTORY = "dataFiles\\tickets\\";

    private Cinema cinema;

    public DataHandler(Cinema cinema) {
        this.cinema = cinema;
    }



    public void getTicket(List<String> fileData) {
        String fileName = setPersonalizedTicketFileName(fileData);
        Path filePath = Path.of(TICKETS_DIRECTORY + fileName);
        try {
            Files.write(filePath, fileData, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Error while printing ticket: " + e.getMessage());
        }
    }

    public boolean changeTicket(String fileName, Seat bookedSeat, Seat newSeat) {
        Path filePath = Path.of(TICKETS_DIRECTORY + fileName);
        try {
            List<String> lines = Files.newBufferedReader(filePath).lines().collect(Collectors.toList());
            if (!lines.toString().contains(bookedSeat.getNumber())) {
                System.out.println("Wrong seat according to the ticket.");
                return false;
            }

            lines.set(4, "Seat: " + newSeat.getNumber());
            lines.set(6, getDateAndTime());

            Files.delete(filePath);
            getTicket(lines);
            return true;
        } catch (IOException e) {
            System.out.println("Error while printing ticket: " + e.getMessage());
            return false;
        }
    }

    public void saveBookedSeat(Seat seat) {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            String seatInfo = "Seat: " + seat.getNumber() + "\n";
            Files.write(filePath, seatInfo.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error while saving booked seat: " + e.getMessage());
        }
    }

    public void loadBookedSeats() {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            List<String> fileLinesList = Files.readAllLines(filePath);
            fileLinesList.forEach(line -> {
                String seatNumber = line.substring(line.indexOf(" ") + 1);
                Seat seat = cinema.getSeat(seatNumber);
                if (seat != null)
                    seat.bookSeat(true);
            });
        } catch (IOException e) {
            System.out.println("Error while loading booked seats: " + e.getMessage());
        }
    }

    public void removeSavedBookedSeat(Seat seat) {
        Path filePath = Path.of(BOOKED_SEATS_FILE);
        try {
            List<String> fileLinesList = Files.readAllLines(filePath);
            fileLinesList.removeIf(i -> i.endsWith(" " + seat.getNumber()));
            Files.write(filePath, fileLinesList);
        } catch (IOException e) {
            System.out.println("Error while overwriting booked seats: " + e.getMessage());
        }
    }

    public String setPersonalizedTicketFileName(List<String> fileData) {
        final int NAME_DATA_INDEX = 1;
        final int SEAT_DATA_INDEX = 4;
        String whitespace = " ";
        String underscore = "_";
        String txtExtension = ".txt";

        String nameData = fileData.get(NAME_DATA_INDEX);
        String retrieveFullName = nameData.substring(nameData.indexOf(whitespace) + 1);
        String seatNumberData = fileData.get(SEAT_DATA_INDEX);
        String retrieveSeatNumber = seatNumberData.substring(seatNumberData.indexOf(whitespace) + 1);

        return retrieveFullName.replace(whitespace, underscore).concat(underscore).concat(retrieveSeatNumber).concat(txtExtension).toLowerCase();
    }

    public static String getDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        return "Date & Time: " + formattedDateTime;
    }
}

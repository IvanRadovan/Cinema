package se.nackademin.cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CinemaExe {

    private static final Cinema cinema = new Cinema("The Matrix");
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataHandler dataHandler = new DataHandler(cinema);

    public static void run() {

        String choice;
        while (true) {
            printMenu();
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> cinema.printSeatsTable();
                case "2" -> book();
                case "3" -> change();
                case "4" -> cancel();
                case "5" -> System.exit(0);
                default -> System.out.println("Choose between options 1 to 5");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Movie: " + cinema.getMovie());
        System.out.println("""
                1. Observe available seats
                2. Book seat
                3. Change seat
                4. Cancel seat
                5. Exit""");
    }

    private static void book() {
        List<String> inputData = new ArrayList<>();
        inputData.add(cinema.getMovie());

        String name = validateInput(
                "Enter your full name: ",
                "Invalid name format. Please enter a valid name.",
                "^[A-Za-z\\s]+$");
        inputData.add("Name: " + name);

        String email = validateInput(
                "Enter your email address: ",
                "Invalid email address. Please enter a valid email.",
                "^[A-Za-z0-9+_.-]+@(.+)$");
        inputData.add("Email address: " + email);

        String phoneNumber = validateInput(
                "Enter you phone number: ",
                "Invalid phone number format. Please enter a valid 10-digit phone number.",
                "^[0-9]{10}$");
        inputData.add("Phone number: " + phoneNumber);

        String seatNumber = validateInput(
                "Choose seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");

        Seat seat = cinema.getSeat(seatNumber.toUpperCase());
        if (cinema.bookSeat(seat)) {
            inputData.add("Seat: " + seat.getNumber());
            inputData.add("Price: " + seat.getPrice() + " SEK");
            inputData.add(DataHandler.getDateAndTime());
            dataHandler.getTicket(inputData);
            System.out.println("Ticket was booked successfully.");
        } else
            System.out.println("No ticket was booked.");
    }

    private static void change() {
        String bookedSeatNumber = validateInput(
                "Enter your seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat bookedSeat = cinema.getSeat(bookedSeatNumber.toUpperCase());

        String newSeatNumber = validateInput(
                "Choose a new seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat newSeat = cinema.getSeat(newSeatNumber.toUpperCase());

        System.out.print("Enter ticket (file) name: ");
        String ticketName = scanner.nextLine().toLowerCase();

        String promptMessage = (cinema.changeSeat(bookedSeat, newSeat, ticketName)) ? "Ticket was changed successfully." : "Unable to change the ticket.";
        System.out.println(promptMessage);
    }

    private static void cancel() {
        String seatNumber = validateInput(
                "Enter seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat seat = cinema.getSeat(seatNumber.toUpperCase());

        String promptMessage = (cinema.cancelSeat(seat)) ? "Ticket was canceled successfully." : "Unable to cancel the ticket.";
        System.out.println(promptMessage);
    }

    private static String validateInput(String promptMessage, String errorMessage, String regexPattern) {
        String input;
        Pattern pattern = Pattern.compile(regexPattern);

        while (true) {
            System.out.print(promptMessage);
            input = scanner.nextLine();
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches())
                return input;
            else
                System.out.println(errorMessage);
        }
    }

    public static void main(String[] args) {
        CinemaExe.run();
    }
}

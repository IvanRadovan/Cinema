package se.nackademin.cinema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CinemaExe {

    private static final MovieDataBase movies = new MovieDataBase();
    private static final Scanner scanner = new Scanner(System.in);
    private static final TextProviderSingleton TEXT_PROVIDER = TextProviderSingleton.getInstance();
    private final PrinterStrategy printStrategy;

    private Movie chosenMovie;


    public CinemaExe(PrinterStrategy printStrategy) {
        this.printStrategy = printStrategy;
    }

    public Movie getChosenMovie() {
        return chosenMovie;
    }

    public void setChosenMovie(Movie chosenMovie) {
        this.chosenMovie = chosenMovie;
    }

    public void run() {
        String choice;

        while (true) {
            printStrategy.printMenu(TEXT_PROVIDER.MAIN_MENU);
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> moviesMenu();
                case "2" -> printStrategy.printMenu(TEXT_PROVIDER.CINEMA_INFO);
                case "3" -> System.out.println("Coming soon...");
                case "4" -> System.exit(0);
                default -> System.out.println("Choose between options 1 to 4");
            }
        }
    }


    private void moviesMenu() {
        movieMenuLoop:
        while (true) {
            printStrategy.printMovies(movies.getMovies());
            String choice = scanner.nextLine();
            String finalChoice = choice;
            Movie movieChosen = movies.getMovies().stream()
                    .filter(movie -> finalChoice.equalsIgnoreCase(movie.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (movieChosen != null) {
                setChosenMovie(movieChosen);
                while (true) {
                    printStrategy.printMenu(TEXT_PROVIDER.INNER_MENU);
                    choice = scanner.nextLine();
                    switch (choice) {
                        case "1" -> movieChosen.getCinema().printSeatsTable();
                        case "2" -> System.out.println(movieChosen);
                        case "3" -> book(movieChosen);
                        case "4" -> change(movieChosen);
                        case "5" -> cancel(movieChosen);
                        case "6" -> { break movieMenuLoop; }
                        default -> System.out.println("Choose between options 1 to 5");
                    }
                }
            } else
                System.out.println("Entered movie was not found.");
        }
    }

    private static void book(Movie movie) {
        List<String> inputData = new ArrayList<>();
        inputData.add("Movie: " + movie.getTitle());

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

        Seat seat = movie.getCinema().getSeat(seatNumber.toUpperCase());
        if (movie.getCinema().bookSeat(seat)) {
            inputData.add("Seat: " + seat.getNumber());
            inputData.add("Price: " + seat.getPrice() + " SEK");
            inputData.add(getDateAndTime());
            movie.getCinema().printTicket(inputData);
            System.out.println("Ticket was booked successfully.");
        } else
            System.out.println("No ticket was booked.");
    }

    private static void change(Movie movie) {
        String bookedSeatNumber = validateInput(
                "Enter you seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat bookedSeat = movie.getCinema().getSeat(bookedSeatNumber.toUpperCase());

        String newSeatNumber = validateInput(
                "Choose a new seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat newSeat = movie.getCinema().getSeat(newSeatNumber.toUpperCase());

        String promptMessage = (movie.getCinema().changeSeat(bookedSeat, newSeat)) ? "Ticket was changed successfully." : "Unable to change the ticket.";
        System.out.println(promptMessage);
    }

    private static void cancel(Movie movie) {
        String seatNumber = validateInput(
                "Enter seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat seat = movie.getCinema().getSeat(seatNumber.toUpperCase());

        String promptMessage = (movie.getCinema().cancelSeat(seat)) ? "Ticket was canceled successfully." : "Unable to cancel the ticket.";
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

    public static String getDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        return "Date & Time: " + formattedDateTime;
    }


    public static void main(String[] args) {
        CinemaExe cinemaExe = new CinemaExe(new PrintToConsole());
        cinemaExe.run();
    }

}

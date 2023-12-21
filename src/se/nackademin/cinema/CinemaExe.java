package se.nackademin.cinema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CinemaExe {

    private final MovieDataBase movies;
    private final Scanner scanner;
    private final TextProviderSingleton textProvider;
    private final PrinterStrategy printer;

    public CinemaExe() {
        this.textProvider = TextProviderSingleton.getInstance();
        this.printer = new PrintToConsole();
        this.movies = MovieDataBase.getInstance();
        this.scanner = new Scanner(System.in);
    }

    private void run() {
        String choice;

        while (true) {
            printer.print(textProvider.MAIN_MENU);
            printer.print("Choose option: ", ANSI.CYAN);
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> runMoviesMenu();
                case "2" -> printer.println(textProvider.CINEMA_INFO, ANSI.YELLOW);
                case "3" -> printer.println(textProvider.WEEKLY_OPENING_HOURS, ANSI.YELLOW);
                case "4" -> System.exit(0);
                default -> printer.println("Choose between options 1 to 4", ANSI.RED);
            }
        }
    }

    private void runMoviesMenu() {
        movieMenuLoop:
        while (true) {
            printMoviesSorted();
            String choice = scanner.nextLine();
            String finalChoice = choice;
            Movie movieChosen = movies.getMovies().stream()
                    .filter(movie -> finalChoice.equalsIgnoreCase(movie.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (movieChosen != null) {
                while (true) {
                    printer.print(textProvider.INNER_MENU);
                    printer.print("Choose option: ", ANSI.CYAN);
                    choice = scanner.nextLine();
                    switch (choice) {
                        case "1" -> movieChosen.getCinema().printSeatsTable();
                        case "2" -> printer.println(movieChosen, ANSI.BLUE);
                        case "3" -> book(movieChosen);
                        case "4" -> cancel(movieChosen);
                        case "5" -> { break movieMenuLoop; }
                        default -> printer.println("Choose between options 1 to 5", ANSI.RED);
                    }
                }
            } else
                printer.println("Invalid movie title.", ANSI.RED);
        }
    }

    private void book(Movie movie) {
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
            printer.println("Ticket was booked successfully.", ANSI.GREEN);
        } else
            printer.println("No ticket was booked.", ANSI.RED);
    }

    private void cancel(Movie movie) {
        String seatNumber = validateInput(
                "Enter seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat seat = movie.getCinema().getSeat(seatNumber.toUpperCase());

        String promptMessage = (movie.getCinema().cancelSeat(seat)) ? "Ticket was canceled successfully." : "Unable to cancel the ticket.";
        printer.println(promptMessage, ANSI.YELLOW);
    }

    private String validateInput(String promptMessage, String errorMessage, String regexPattern) {
        String input;
        Pattern pattern = Pattern.compile(regexPattern);

        while (true) {
            System.out.print(promptMessage);
            input = scanner.nextLine();
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches())
                return input;
            else
                printer.println(errorMessage, ANSI.RED);
        }
    }

    public String getDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = currentDateTime.format(formatter);
        return "Date & Time: " + formattedDateTime;
    }

    private void printMoviesSorted() {
        final String HYPHEN = "-";
        printer.println("Listed movies:");
        movies.getMovies()
                .stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .forEach(movie -> printer.println(HYPHEN.concat(movie.getTitle()), ANSI.MAGENTA));
        printer.print("Enter a movie: ", ANSI.CYAN);
    }

    public static void main(String[] args) {
        CinemaExe cinemaExe = new CinemaExe();
        cinemaExe.run();
    }

}

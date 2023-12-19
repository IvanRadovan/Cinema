package se.nackademin.cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CinemaExe {


    private static final Movies movies = new Movies();
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        String choice;

        while (true) {
            printMenu();
            choice = scanner.nextLine();

            switch (choice) {
                case "1" -> moviesMenu();
                case "2" -> printCinemaInfo();
                case "3" -> System.out.println("Coming soon...");
                case "4" -> System.exit(0);
                default -> System.out.println("Choose between options 1 to 4");
            }
        }
    }

    private static void moviesMenu() {
        while (true) {
            printListedMovies();
            String choice = scanner.nextLine();
            String finalChoice = choice;
            Movie movieChosen = movies.getMovieList().stream()
                    .filter(movie -> finalChoice.equalsIgnoreCase(movie.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (movieChosen != null) {
                innerLoop:
                while (true) {
                    printSelectedMovieMenu();
                    choice = scanner.nextLine();
                    switch (choice) {
                        case "1" -> movieChosen.getCinema().printSeatsTable();
                        case "2" -> System.out.println(movieChosen);
                        case "3" -> book(movieChosen);
                        case "4" -> change(movieChosen);
                        case "5" -> cancel(movieChosen);
                        case "6" -> { break innerLoop; }
                        default -> System.out.println("Choose between options 1 to 5");
                    }
                }
            } else
                break;
        }
    }

    private static void printMenu() {
        System.out.println("""
                1. Listed movies
                2. Cinema info
                3. Subscribe
                4. Exit""");
    }

    private static void printSelectedMovieMenu() {
        System.out.println("""
                1. Observe available seats
                2. Movie info
                3. Book seat
                4. Change seat
                5. Cancel seat
                6. Back""");
    }

    private static void printListedMovies() {
        System.out.println("What movie do you want to watch?");
        for (int i = 0; i < movies.getMovieList().size(); i++) {
            System.out.printf("%s. %s%n", i, movies.getMovieList().get(i));
        }
    }

    private static void printCinemaInfo() {
        System.out.println("""
                Cinema Name: QuantumTime Cinemas
                Location: Storgatan 51, Stockholm
                Phone Number: +46 8 555 123 456
                Opening Time: 14:00
                Closing Time: 02:00
                                
                Cutting-Edge Technology:
                                
                State-of-the-art laser projectors for crystal-clear visuals
                3D and 4D immersive experiences in select theaters
                Virtual Reality (VR) lounge for pre-show entertainment
                Special Events:
                                
                Time-Travel Thursdays: Classic films and cult favorites from various eras
                Quantum Nights: Exclusive screenings of avant-garde and experimental films
                Contactless Experience:
                                
                Mobile ticketing and contactless payment options
                Interactive holographic concierge for assistance
                Themed Auditoriums:
                                
                Steampunk Studio: A theater with a Victorian-era time-travel theme
                Cyber Future Hall: Futuristic aesthetics and cutting-edge technology
                Culinary Delights:
                                
                Molecular Gastronomy Popcorn Lab: Ever-changing popcorn flavors created with scientific flair
                Time Warp Café: A themed café offering international cuisine inspired by different time periods
                Environmental Initiatives:
                                
                Eco-friendly architecture with solar panels and green roofs
                Zero-waste initiatives, including compostable popcorn containers
                """);
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
            inputData.add(DataHandler.getDateAndTime());
            DataHandler.getInstance(movie).printTicket(inputData);
            System.out.println("Ticket was booked successfully.");
        } else
            System.out.println("No ticket was booked.");
    }

    private static void change(Movie movie) {
        String bookedSeatNumber = validateInput(
                "Enter your seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat bookedSeat = movie.getCinema().getSeat(bookedSeatNumber.toUpperCase());

        String newSeatNumber = validateInput(
                "Choose a new seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat newSeat = movie.getCinema().getSeat(newSeatNumber.toUpperCase());

        System.out.print("Enter ticket (file) name: ");
        String ticketName = scanner.nextLine().toLowerCase();

        String promptMessage = (movie.getCinema().changeSeat(bookedSeat, newSeat, ticketName)) ? "Ticket was changed successfully." : "Unable to change the ticket.";
        System.out.println(promptMessage);
    }

    private static void cancel(Movie movie) {
        String seatNumber = validateInput(
                "Enter seat number: ",
                "Invalid seat number. Please choose a seat between A01-H08.",
                "^[A-Ha-h]0[0-8]$");
        Seat seat = movie.getCinema().getSeat(seatNumber.toUpperCase());

        String promptMessage = (movie.getCinema().cancelSeat(movie.getTitle(), seat)) ? "Ticket was canceled successfully." : "Unable to cancel the ticket.";
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

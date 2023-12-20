package se.nackademin.cinema;

import java.util.List;

public class PrintToConsole implements PrinterStrategy {


    @Override
    public void printMenu(String text) {
        System.out.println(text);
    }

    @Override
    public void printMenu(List<String> text) {
        text.forEach(System.out::println);
    }

    @Override
    public void printMovies(List<Movie> movies) {
        System.out.println("Enter what movie do you want to watch?");
        movies.forEach(movie -> System.out.println("\t" + movie.getTitle()));
    }
}

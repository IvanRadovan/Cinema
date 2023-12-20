package se.nackademin.cinema;

import java.util.List;

public interface PrinterStrategy {

    void printMenu(String text);
    void printMenu(List<String> text);
    void printMovies(List<Movie> movies);

}

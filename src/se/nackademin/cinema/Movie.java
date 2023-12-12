package se.nackademin.cinema;

import java.util.List;

public class Movie {

    private String title;
    private String plot;
    private String genre;
    private String duration;
    private String director;
    private List<String> stars;
    private double ticketPrice;

    public Movie(String title, String plot, String genre, String duration, String director, List<String> stars, double ticketPrice) {
        this.title = title;
        this.plot = plot;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
        this.stars = stars;
        this.ticketPrice = ticketPrice;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public String getDirector() {
        return director;
    }

    public List<String> getStars() {
        return stars;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
}

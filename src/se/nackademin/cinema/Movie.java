package se.nackademin.cinema;

public class Movie {

    private String title;
    private String plot;
    private String genre;
    private String duration;
    private String director;
    private String stars;
    private double ticketPrice;

    public Movie(String title, String plot, String genre, String duration, String director, String stars, double ticketPrice) {
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

    public String getStars() {
        return stars;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return """
                %S:
                Ticket price: %s SEK
                Genre: %s
                Duration: %s
                Director: %s
                Stars: %s
                Plot:
                %s
                """.formatted(title, ticketPrice, genre, duration, director, stars, plot);
    }
}

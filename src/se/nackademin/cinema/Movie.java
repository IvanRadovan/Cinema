package se.nackademin.cinema;

public class Movie {

    private final String title;
    private final String plot;
    private final String genre;
    private final String duration;
    private final String director;
    private final String stars;
    private final double ticketPrice;
    private final int rating;
    private final boolean in3D;
    private final Cinema cinema;

    private Movie(MovieBuilder builder) {
        this.title = builder.title;
        this.plot = builder.plot;
        this.genre = builder.genre;
        this.duration = builder.duration;
        this.director = builder.director;
        this.stars = builder.stars;
        this.ticketPrice = builder.ticketPrice;
        this.rating = builder.rating;
        this.in3D = builder.in3D;
        cinema = new Cinema(this.title, this.ticketPrice);
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

    public int getRating() {
        return rating;
    }

    public boolean isIn3D() {
        return in3D;
    }

    public Cinema getCinema() {
        return cinema;
    }

    @Override
    public String toString() {
        String genre = this.genre != null ? "Genre: " + this.genre : "";
        String duration = this.duration != null ? "Duration: " + this.duration : "";
        String director = this.director != null ? "Director: " + this.director : "";
        String stars = this.stars != null ? "Stars: " + this.stars : "";
        String plot = this.plot != null ? "Plot: " + this.plot : "";
        String ratingInfo = this.rating >= 1 ? "Rating: " + this.rating : "";
        String in3DInfo = this.in3D ? "In 3D: Yes" : "";

        return """
            Movie: %s:
            Ticket price: %s SEK
            %s
            %s
            %s
            %s
            %s
            %s
            %s
            """.formatted(this.title, this.ticketPrice, genre, plot, duration, director, stars, ratingInfo, in3DInfo).trim();
    }


    public static class MovieBuilder {

        private final String title;
        private final double ticketPrice;

        private String plot;
        private String genre;
        private String duration;
        private String director;
        private String stars;
        private int rating;
        private boolean in3D;

        private static final int ZERO = 0;
        private static final int LOWEST_RATING = 10;
        private static final int HIGHEST_RATING = 10;
        private static final String RATING_PROMPT = "Invalid rating value: Rate is from 1 - 10.";
        private static final String PRICE_PROMPT = "Invalid price ticket: Price cannot be lower than 0";

        public MovieBuilder(String title, double ticketPrice) {
            this.title = title;
            if (ticketPrice < ZERO) throw new IllegalArgumentException(PRICE_PROMPT);
            this.ticketPrice = ticketPrice;
        }

        public MovieBuilder setPlot(String plot) {
            this.plot = plot;
            return this;
        }

        public MovieBuilder setGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public MovieBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public MovieBuilder setDirector(String director) {
            this.director = director;
            return this;
        }

        public MovieBuilder setStars(String stars) {
            this.stars = stars;
            return this;
        }

        public MovieBuilder setRating(int rating) {
            if (rating < LOWEST_RATING || rating > HIGHEST_RATING)
                throw new IllegalArgumentException(RATING_PROMPT);
            this.rating = rating;
            return this;
        }

        public MovieBuilder setIn3D(boolean in3D) {
            this.in3D = in3D;
            return this;
        }

        public Movie build(){
            return new Movie(this);
        }
    }
}

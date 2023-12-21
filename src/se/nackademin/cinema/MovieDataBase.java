package se.nackademin.cinema;

import java.util.List;

public class MovieDataBase {

    private final List<Movie> movies;
    private static MovieDataBase instance;

    private MovieDataBase() {
        this.movies = List.of(MOVIE_ONE, MOVIE_TWO, MOVIE_THREE, MOVIE_FOUR);
    }

    public static MovieDataBase getInstance() {
        if (instance == null) {
            instance = new MovieDataBase();
        }
        return instance;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    private static final Movie MOVIE_ONE = new Movie.MovieBuilder("Inception", 100)
            .setPlot("A heist story in dreams")
            .setGenre("Sci-Fi")
            .setDuration("2h 28min")
            .setDirector("Christopher Nolan")
            .setStars("Leonardo DiCaprio | Elliot Page | Tom Hardy ")
            .build();

    private static final Movie MOVIE_TWO = new Movie.MovieBuilder("The Dark Knight", 200)
            .setPlot("A masked vigilante battles crime")
            .setGenre("Action")
            .setDuration("2h 32min")
            .setDirector("Christopher Nolan")
            .setStars("Christian Bale | Heath Ledger | Aaron Eckhart")
            .setRating(10)
            .build();

    private static final Movie MOVIE_THREE = new Movie.MovieBuilder("The Shawshank Redemption", 150)
            .setPlot("Two imprisoned men bond over several years")
            .setGenre("Drama")
            .setDuration("2h 22min")
            .setDirector("Frank Darabont")
            .setStars("Tim Robbins | Morgan Freeman")
            .build();

    private static final Movie MOVIE_FOUR = new Movie.MovieBuilder("Red Room", 250)
            .build();

}

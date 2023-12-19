package se.nackademin.cinema;

import java.util.ArrayList;
import java.util.List;

public class Movies {

    private List<Movie> movieList;

    public Movies() {
        this.movieList = new ArrayList<>();
        movieAdder();
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    private void movieAdder() {
        movieList.add(matrix);
        movieList.add(matrix_II);
    }

    private final Movie matrix_II = new Movie(
            "The Matrix II",
            """
                    When a beautiful stranger leads computer hacker Neo to a forbidding underworld,
                    he discovers the shocking truth.
                    The life he knows is the elaborate deception of an evil cyber-intelligence.""",
            "Sci-Fi",
            "2h 16min",
            "Lana Wachowski & Lilly Wachowski",
            "Keanu Reeves - Laurence Fishburne - Carrie-Anne Moss",
            150.0);

    private final Movie matrix = new Movie(
            "The Matrix",
            """
                    When a beautiful stranger leads computer hacker Neo to a forbidding underworld,
                    he discovers the shocking truth.
                    The life he knows is the elaborate deception of an evil cyber-intelligence.""",
            "Sci-Fi",
            "2h 16min",
            "Lana Wachowski & Lilly Wachowski",
            "Keanu Reeves - Laurence Fishburne - Carrie-Anne Moss",
            150.0);



}

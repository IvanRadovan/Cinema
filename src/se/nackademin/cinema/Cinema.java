package se.nackademin.cinema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cinema {

    private final String movie;
    private final int rowSize;
    private final int columnSize;
    private final Set<Seat> seats;


    public Cinema() {
        this("No movie playing", 5, 5);
    }

    public Cinema(String movie, int rowSize, int columnSize) {
        if ((rowSize < 5 || rowSize > 12) || (columnSize < 5 || columnSize > 12)) {
            throw new IllegalArgumentException("Illegal arguments passed to the constructor");
        }
        this.movie = movie;
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.seats = new HashSet<>();

        IntFunction<Seat> seatNameGenerator = i -> {
            char row = (char) ('A' + i / columnSize);
            int column = i % columnSize + 1;
            return String.format("%s%02d", row, column);
        };

        seats = IntStream.range(0, rowSize * columnSize)
                .mapToObj(seatNameGenerator)
                .collect(Collectors.toSet());
    }

    public String getMovie() {
        return movie;
    }


    protected boolean bookSeat(String seatNr) {


        return true;
    }


}

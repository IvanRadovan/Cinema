import java.util.Arrays;

public class Cinema {

    private final String movie;
    private final int rowSize;
    private final int columnSize;
    private final String[] seats;


    public Cinema(String movie, int rowSize, int columnSize) {
        if ((rowSize < 5 || rowSize > 12) && (columnSize < 5 || columnSize > 12)) {
            throw new IllegalArgumentException("Illegal arguments passed to the constructor");
        }
        this.movie = movie;
        this.rowSize = rowSize;
        this.columnSize = columnSize;


        char startingSeat = 0;
        char row = 0;
        int counter = 0;
        this.seats = new String[rowSize * columnSize];
        for (char currentSeat = startingSeat; currentSeat < seats.length; currentSeat++) {
            int column = (currentSeat % columnSize) + 1;
            if (currentSeat % rowSize == currentSeat) {
                row = (char) (counter + 'A');
            }
            this.seats[currentSeat] = String.format("%s%02d", row, column);
        }
    }

    public Cinema() {
        this("No movie playing", 5, 5);
    }


    @Override
    public String toString() {
        return Arrays.toString(seats);
    }
}

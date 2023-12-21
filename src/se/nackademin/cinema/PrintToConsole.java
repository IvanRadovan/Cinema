package se.nackademin.cinema;

public class PrintToConsole implements PrinterStrategy {

    @Override
    public <T> void print(T text) {
        System.out.print(text);
    }

    @Override
    public <T> void println(T text) {
        System.out.println(text);
    }

    @Override
    public <T> void print(T text, ANSI color) {
        System.out.printf("%s%s%s", color, text, ANSI.RESET);
    }

    @Override
    public <T> void println(T text, ANSI color) {
        System.out.printf("%s%s%s%n", color, text, ANSI.RESET);
    }
}

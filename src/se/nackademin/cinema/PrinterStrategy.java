package se.nackademin.cinema;

public interface PrinterStrategy {

    <T> void print(T text);

    <T> void println(T text);

    <T> void print(T text, ANSI color);

    <T> void println(T text, ANSI color);
}

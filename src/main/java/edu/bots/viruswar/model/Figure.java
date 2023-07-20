package edu.bots.viruswar.model;
public class Figure {
    // TODO: update to better chars
    public static final char EMPTY = '.';
    public static final char CROSS = 'X';
    public static final char CIRCLE = 'O';
    public static final char KILLED_CROSS = '#';
    public static final char KILLED_CIRCLE = '@';

    public static char kill(char figure) {
        if (figure == CROSS)
            return KILLED_CROSS;
        if (figure == CIRCLE)
            return KILLED_CIRCLE;

        throw new IllegalArgumentException("Cannot kill " + figure);
    }

    public static char other(char figure) {
        if (figure == CROSS)
            return CIRCLE;
        if (figure == CIRCLE)
            return CROSS;
        if (figure == KILLED_CROSS)
            return KILLED_CIRCLE;
        if (figure == KILLED_CIRCLE)
            return KILLED_CROSS;

        throw new IllegalArgumentException("No other for " + figure);
    }
}

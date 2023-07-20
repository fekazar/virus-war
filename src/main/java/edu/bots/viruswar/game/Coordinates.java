package edu.bots.viruswar.game;

import java.util.Optional;

public record Coordinates(int x, int y) {
    public static Optional<Coordinates> parse(String s) {
        var spl = s.split(" ");

        if (spl.length < 2 || spl[0].length() != 1 || spl[1].length() != 1)
            return Optional.empty();

        try {
            int x = spl[0].charAt(0) - 'a';
            int y = Integer.parseInt(spl[1]) - 1;

            // TODO: inject field size from properties
            if (x > 9 || x < 0 || y > 10 || y < 0)
                return Optional.empty();

            return Optional.of(new Coordinates(x, y));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

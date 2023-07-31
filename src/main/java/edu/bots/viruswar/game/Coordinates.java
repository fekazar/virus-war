package edu.bots.viruswar.game;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public record Coordinates(int x, int y) {
    public static Optional<Coordinates> parse(String s) {
        var spl = s.toLowerCase().split(" ");

        if (spl.length < 2)
            return Optional.empty();

        try {
            int x = spl[0].charAt(0) - 'a';
            int y = Integer.parseInt(spl[1]) - 1;

            // TODO: inject field size from properties
            if (x >= 10 || x < 0 || y >= 10 || y < 0)
                return Optional.empty();

            return Optional.of(new Coordinates(x, y));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toGameRep() {
        return String.format("%s %s", (char) ('a' + x), y + 1);
    }
}

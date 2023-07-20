package edu.bots.viruswar;

import edu.bots.viruswar.game.Coordinates;
import edu.bots.viruswar.game.FieldRender;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class GameUtilTest {
    @Test
    void canTurnTest() {
        var utils = new GameUtils(3, 3, 3);

        var session = new Session();
        session.setMove(100500);
        session.setField("X@@\n@@O\n@OO\n");

        assertTrue(utils.canTurn(session, 'X'));
        assertFalse(utils.canTurn(session, 'O'));
    }

    @ParameterizedTest
    @MethodSource("fieldSource")
    void eatTest(int w, int h, String field, boolean can, int x, int y) { // x and y are 0-indexed coordinates
        var utils = new GameUtils(w, h, 3);

        var session = new Session();
        session.setMove(100500);
        session.setField(field);

        assertEquals(can, utils.update(new Coordinates(x, y), 'X', session));
    }

    public static Stream<Arguments> fieldSource() {
        return Stream.of(
                Arguments.of(7, 3, """
                .......
                X@@@@@O
                ....... 
                """, true, 6, 1),

                Arguments.of(8, 3, """
                ........
                X@@@@@.O
                ........ 
                """, false, 7, 1),

                Arguments.of(8, 3, """
                ......O.
                X@@@@@.O
                ........ 
                """, true, 6, 0));
    }
}

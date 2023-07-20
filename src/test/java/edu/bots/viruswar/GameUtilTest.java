package edu.bots.viruswar;

import edu.bots.viruswar.game.FieldRender;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Session;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
}

package edu.bots.viruswar.service.state;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.game.Coordinates;
import edu.bots.viruswar.game.FieldRender;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Figure;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;

import java.util.function.Consumer;

public class AwaitCoordinatesStateHandler implements StateHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;
    private final GameUtils gameUtils;

    private final int maxOps = 3; // TODO: inject max ops from properties

    public AwaitCoordinatesStateHandler(PlayerRepository playerRepository, SessionRepository sessionRepository, GameUtils gameUtils) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
        this.gameUtils = gameUtils;
    }

    @Override
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        /*
            TODO:
             1. validate coordinates
             2. check if it's possible to put a figure there
             3. put a figure, update moves
         */

        var coordsOpt = Coordinates.parse(msg);
        if (coordsOpt.isEmpty()) {
            onAnswer.accept(new ServiceAnswer("Enter correct coordinates", playerId, new ForceReply()));
            return;
        }

        var coords = coordsOpt.get();
        var session = sessionRepository.findByPlayerId(playerId).get();
        var curPlayer = playerRepository.findById(playerId).get();

        // for now there is no game logic...
        boolean hasTurned = gameUtils.update(coords, curPlayer.getPlaysWith(), session);
        if (hasTurned) {
            session.setMove(session.getMove() + 1);
            session = sessionRepository.save(session);

            var rendered = FieldRender.render(session.getMappedField());

            onAnswer.accept(new ServiceAnswer("Opponent's turn to: " + coords + ".\n Cur map:\n" + rendered,
                    session.otherPlayer(playerId), null));

            onAnswer.accept(new ServiceAnswer("Current map:\n" + rendered, playerId, null));

            if (!gameUtils.canTurn(session.getMappedField(), Figure.other(curPlayer.getPlaysWith()))) {
                onAnswer.accept(new ServiceAnswer("You won! Session was deleted.", playerId, null));
                onAnswer.accept(new ServiceAnswer("You lose! Session was deleted.", session.otherPlayer(playerId), null));

                var otherPlayer = playerRepository.findById(session.otherPlayer(playerId)).get();

                curPlayer.setState(Player.State.DEFAULT);
                otherPlayer.setState(Player.State.DEFAULT);
                sessionRepository.delete(session);

                return;
            }

            if (session.getMove() % maxOps == 0) {
                var otherPlayer = playerRepository.findById(session.otherPlayer(playerId)).get();

                curPlayer.setState(Player.State.AWAITS_OTHER_PLAYER);
                otherPlayer.setState(Player.State.AWAITS_COORDINATES);

                playerRepository.save(curPlayer);
                playerRepository.save(otherPlayer);

                onAnswer.accept(new ServiceAnswer("Your move is over.", playerId, null));
                onAnswer.accept(new ServiceAnswer("It's your move! Enter coords: ", session.otherPlayer(playerId), new ForceReply()));
            } else {
                onAnswer.accept(new ServiceAnswer("Enter another coords...", curPlayer.getId(), new ForceReply()));
            }
        } else {
            onAnswer.accept(new ServiceAnswer("Unavailable. Enter another coords...", curPlayer.getId(), new ForceReply()));
        }
    }
}

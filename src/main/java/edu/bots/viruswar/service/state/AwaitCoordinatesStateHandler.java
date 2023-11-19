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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
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
    @Transactional
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        var coordsOpt = Coordinates.parse(msg);
        if (coordsOpt.isEmpty()) {
            onAnswer.accept(new ServiceAnswer("Введите корректные координаты.", playerId, new ForceReply()));
            return;
        }

        var coords = coordsOpt.get();
        var session = sessionRepository.findByPlayerId(playerId).get();
        var curPlayer = playerRepository.findById(playerId).get();

        // for now there is no game logic...
        boolean hasTurned = gameUtils.update(coords, curPlayer.getPlaysWith(), session);
        if (hasTurned) {
            session.setMove(session.getMove() + 1);
            //session = sessionRepository.save(session);

            var rendered = FieldRender.render(session.getMappedField());

            onAnswer.accept(new ServiceAnswer("Ход противника: " + coords.toGameRep() + ". Поле:\n" + rendered,
                    session.otherPlayer(playerId), null));

            onAnswer.accept(new ServiceAnswer("Поле:\n" + rendered, playerId, null));

            if (!gameUtils.canTurn(session, Figure.other(curPlayer.getPlaysWith()))) {
                onAnswer.accept(new ServiceAnswer("Вы выиграли! Сессия окончена.", playerId, null));
                onAnswer.accept(new ServiceAnswer("Вы проиграли! Сессия окончена.", session.otherPlayer(playerId), null));

                var otherPlayer = playerRepository.findById(session.otherPlayer(playerId)).get();

                curPlayer.setState(Player.State.DEFAULT);
                otherPlayer.setState(Player.State.DEFAULT);
                sessionRepository.delete(session);

                return;
            }

            // TODO: remove code repeat
            if (!gameUtils.canTurn(session, curPlayer.getPlaysWith())) {
                onAnswer.accept(new ServiceAnswer("Вы выиграли! Сессия окончена.", session.otherPlayer(playerId), null));
                onAnswer.accept(new ServiceAnswer("Вы проиграли! Сессия окончена.", playerId, null));

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

                //playerRepository.save(curPlayer);
                //playerRepository.save(otherPlayer);

                onAnswer.accept(new ServiceAnswer("Ход окончен.", playerId, null));
                onAnswer.accept(new ServiceAnswer("Ваш ход! Введите координаты: ", session.otherPlayer(playerId), new ForceReply()));
            } else {
                onAnswer.accept(new ServiceAnswer("Введите координаты: ", curPlayer.getId(), new ForceReply()));
            }
        } else {
            onAnswer.accept(new ServiceAnswer("Невозможно, введите другие координаты: ", curPlayer.getId(), new ForceReply()));
        }
    }
}

package edu.bots.viruswar.service.state;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Figure;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

public class AwaitConnectIdStateHandler implements StateHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;
    private final GameUtils gameUtils;

    public AwaitConnectIdStateHandler(PlayerRepository playerRepository, SessionRepository sessionRepository, GameUtils gameUtils) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
        this.gameUtils = gameUtils;
    }

    @Transactional
    @Override
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findById(msg);
        var client = playerRepository.findById(playerId).get();

        if (sessionOpt.isEmpty()) {
            onAnswer.accept(new ServiceAnswer("There is no such session.", playerId, null));
            client.setState(Player.State.DEFAULT);
            playerRepository.save(client);
            return;
        }

        var session = sessionOpt.get();
        if (session.getClientId() != null) {
            onAnswer.accept(new ServiceAnswer("Unavailable session.", playerId, null));
            return;
        }

        var host = playerRepository.findById(session.getHostId()).get();

        // TODO: Concurrency???
        session.setClientId(client.getId());
        session.setMappedField(gameUtils.getEmptyField());

        host.setState(Player.State.AWAITS_COORDINATES);
        client.setState(Player.State.AWAITS_OTHER_PLAYER);

        host.setPlaysWith(Figure.CROSS);
        client.setPlaysWith(Figure.CIRCLE);

        sessionRepository.save(session);
        playerRepository.save(host);
        playerRepository.save(client);

        onAnswer.accept(new ServiceAnswer("You have connected to the session. Starting a game (not yet).", playerId, null));
        onAnswer.accept(new ServiceAnswer("Player has connected to the session. Starting a game (not yet).", host.getId(), null));
        onAnswer.accept(new ServiceAnswer("Enter coordinates: ", host.getId(), new ForceReply()));
    }
}

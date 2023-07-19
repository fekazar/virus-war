package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

// TODO: remove this state, make deletion without request for sessionId

@Slf4j
public class AwaitDeleteStateHandler implements StateHandler {
    private final SessionRepository sessionRepository;
    private final PlayerRepository playerRepository;

    public AwaitDeleteStateHandler(SessionRepository sessionRepository, PlayerRepository playerRepository) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        log.info("pl id: " + playerId);
        log.info("msg: " + msg);

        sessionRepository.deleteBySessionId(msg);

        var player = playerRepository.findById(playerId).get();
        player.setState(Player.State.DEFAULT);
        playerRepository.save(player);

        // TODO: notify clietn as well
        onAnswer.accept(new ServiceAnswer("Session deleted.", playerId, null));
    }
}

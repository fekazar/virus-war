package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwaitDeleteStateHandler implements StateHandler {
    private final SessionRepository sessionRepository;
    private final PlayerRepository playerRepository;

    public AwaitDeleteStateHandler(SessionRepository sessionRepository, PlayerRepository playerRepository) {
        this.sessionRepository = sessionRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public ServiceAnswer handle(Long playerId, String msg) {
        log.info("pl id: " + playerId);
        log.info("msg: " + msg);

        sessionRepository.deleteBySessionId(msg);

        var player = playerRepository.findById(playerId).get();
        player.setState(Player.State.DEFAULT);
        playerRepository.save(player);

        return new ServiceAnswer("Session deleted.", null);
    }
}

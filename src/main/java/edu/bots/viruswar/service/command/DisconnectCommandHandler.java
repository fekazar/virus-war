package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;

import java.util.function.Consumer;

public class DisconnectCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    public DisconnectCommandHandler(PlayerRepository playerRepository, SessionRepository sessionRepository) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isEmpty()) {
            onAnswer.accept(new ServiceAnswer("You are not connected to any session", playerId, null));
            return;
        }

        // TODO: what is player is in game?
        var session = sessionOpt.get();
        sessionRepository.delete(session);

        onAnswer.accept(new ServiceAnswer("You've been disconnected from session.", playerId, null));
    }
}

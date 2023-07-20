package edu.bots.viruswar.service.command;

import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.model.Session;
import edu.bots.viruswar.repository.SessionRepository;
import jakarta.transaction.Transactional;

import java.util.function.Consumer;

public class CreateCommandHandler implements CommandHandler {
    private final SessionRepository sessionRepository;
    private final GameUtils gameUtils;

    public CreateCommandHandler(SessionRepository sessionRepository, GameUtils gameUtils) {
        this.sessionRepository = sessionRepository;
        this.gameUtils = gameUtils;
    }

    @Override
    @Transactional
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isPresent()) {
            onAnswer.accept(new ServiceAnswer("You already have an active session. Id: " + sessionOpt.get().getSessionId(), playerId, null));
            return;
        }

        var session = new Session();
        session.setHostId(playerId);
        session.setMappedField(gameUtils.getEmptyField());

        session = sessionRepository.save(session);

        onAnswer.accept(new ServiceAnswer("You session: " + session.getSessionId(), playerId, null));
    }
}

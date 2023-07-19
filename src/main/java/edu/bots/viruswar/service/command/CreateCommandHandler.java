package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.model.Session;
import edu.bots.viruswar.repository.SessionRepository;
import jakarta.transaction.Transactional;

import java.util.function.Consumer;

public class CreateCommandHandler implements CommandHandler {
    private final SessionRepository sessionRepository;

    public CreateCommandHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
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
        session.setMove(1);

        session = sessionRepository.save(session);

        onAnswer.accept(new ServiceAnswer("You session: " + session.getSessionId(), playerId, null));
    }
}

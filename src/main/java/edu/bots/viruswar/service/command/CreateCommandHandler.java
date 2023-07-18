package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.model.Session;
import edu.bots.viruswar.repository.SessionRepository;
import jakarta.transaction.Transactional;

public class CreateCommandHandler implements CommandHandler {
    private final SessionRepository sessionRepository;

    public CreateCommandHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public ServiceAnswer handle(Long playerId, String command) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isPresent()) {
            return new ServiceAnswer("You already have an active session. Id: " + sessionOpt.get().getSessionId(), false);
        }

        var session = new Session();
        session.setHostId(playerId);
        session.setMove(1);

        session = sessionRepository.save(session);

        return new ServiceAnswer("You session: " + session.getSessionId(), false);
    }
}

package edu.bots.viruswar.service.command;

import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.model.Session;
import edu.bots.viruswar.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component("/create")
public class CreateCommandHandler implements CommandHandler {
    private final SessionRepository sessionRepository;
    private final GameUtils gameUtils;

    public CreateCommandHandler(SessionRepository sessionRepository, GameUtils gameUtils) {
        this.sessionRepository = sessionRepository;
        this.gameUtils = gameUtils;
    }

    @Override
    public void handle(Long playerId, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isPresent()) {
            onAnswer.accept(new ServiceAnswer("У вас уже есть активная сессия. Id: " + sessionOpt.get().getSessionId(), playerId, null));
            return;
        }

        var session = new Session();
        session.setHostId(playerId);
        session.setMappedField(gameUtils.getEmptyField());

        session = sessionRepository.save(session);

        onAnswer.accept(new ServiceAnswer("Id вашей сессии: " + session.getSessionId(), playerId, null));
    }
}

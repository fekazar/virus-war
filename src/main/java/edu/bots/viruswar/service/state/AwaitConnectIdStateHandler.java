package edu.bots.viruswar.service.state;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.game.FieldRender;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Figure;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Component
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
            onAnswer.accept(new ServiceAnswer("Такой сессии нет.", playerId, null));
            client.setState(Player.State.DEFAULT);
            //playerRepository.save(client);
            return;
        }

        var session = sessionOpt.get();
        if (session.getClientId() != null) {
            onAnswer.accept(new ServiceAnswer("Недоступная сессия.", playerId, null));
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

        //sessionRepository.save(session);
        //playerRepository.save(host);
        //playerRepository.save(client);

        onAnswer.accept(new ServiceAnswer("Вы подключились к сессии, игра начинается.", playerId, null));
        onAnswer.accept(new ServiceAnswer("Кто-то подключился к сессии, игра начинается.", host.getId(), null));

        onAnswer.accept(new ServiceAnswer("Поле: \n" + FieldRender.render(session.getMappedField()), host.getId(), null));
        onAnswer.accept(new ServiceAnswer("Введите координаты: ", host.getId(), new ForceReply()));
    }
}

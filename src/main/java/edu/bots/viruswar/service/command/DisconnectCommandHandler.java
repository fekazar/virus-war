package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Slf4j
public class DisconnectCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    public DisconnectCommandHandler(PlayerRepository playerRepository, SessionRepository sessionRepository) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isEmpty()) {
            onAnswer.accept(new ServiceAnswer("Вы не подключены ни к какой сессии.", playerId, null));
            return;
        }

        var session = sessionOpt.get();
        var host = playerRepository.findById(session.getHostId()).get();
        host.setPlaysWith(null);
        host.setState(Player.State.DEFAULT);
        playerRepository.save(host);

        var clientId = session.getClientId();
        if (clientId != null) {
            var client = playerRepository.findById(clientId).get();
            client.setPlaysWith(null);
            client.setState(Player.State.DEFAULT);
            playerRepository.save(client);

            onAnswer.accept(new ServiceAnswer("Оппонент покинул игру. Вы отключены от сессии.", session.otherPlayer(playerId), null));
        }

        sessionRepository.deleteBySessionId(session.getSessionId());
        onAnswer.accept(new ServiceAnswer("Вы были отключены от сессии.", playerId, null));
    }
}

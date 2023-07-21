package edu.bots.viruswar.service.command;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;

import java.util.function.Consumer;

public class ConnectCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    public ConnectCommandHandler(PlayerRepository playerRepository, SessionRepository sessionRepository) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);

        if (sessionOpt.isPresent()) {
            onAnswer.accept(new ServiceAnswer("Вы уже подключены к какой-то сессии.", playerId, null));
            return;
        }

        var player = playerRepository.findById(playerId).get();
        player.setState(Player.State.AWAITS_CONNECT_ID);
        playerRepository.save(player);

        onAnswer.accept(new ServiceAnswer("Введите Id сессии:", playerId, new ForceReply()));
    }
}

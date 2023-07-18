package edu.bots.viruswar.service.command;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;

public class DeleteCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;
    private final SessionRepository sessionRepository;

    public DeleteCommandHandler(PlayerRepository playerRepository, SessionRepository sessionRepository) {
        this.playerRepository = playerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public ServiceAnswer handle(Long playerId, String command) {
        var sessionOpt = sessionRepository.findByPlayerId(playerId);
        if (sessionOpt.isEmpty())
            return new ServiceAnswer("You don't have a session with this id.", null);

        var player = playerRepository.findById(playerId).get();
        player.setState(Player.State.AWAITS_DELETE_ID);
        playerRepository.save(player);

        return new ServiceAnswer("Enter session id:", new ForceReply());
    }
}

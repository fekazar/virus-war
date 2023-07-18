package edu.bots.viruswar.service.command;


import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;

public class StartCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;

    public StartCommandHandler(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public ServiceAnswer handle(Long playerId, String command) {
        var playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            var player = new Player();
            player.setId(playerId);
            playerRepository.save(player);

            return new ServiceAnswer("Welcome, new player!", false);
        }

        return new ServiceAnswer("Welcome, old player", false);
    }
}

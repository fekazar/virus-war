package edu.bots.viruswar.service.command;


import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;

import java.util.function.Consumer;

public class StartCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;

    public StartCommandHandler(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        var playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            var player = new Player();
            player.setId(playerId);
            playerRepository.save(player);

            onAnswer.accept(new ServiceAnswer("Welcome, new player!", playerId, null));
            return;
        }

        onAnswer.accept(new ServiceAnswer("Welcome, old player", playerId, null));
    }
}

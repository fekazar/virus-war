package edu.bots.viruswar.service.command;


import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component("/start")
public class StartCommandHandler implements CommandHandler {
    private final PlayerRepository playerRepository;

    public StartCommandHandler(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void handle(Long playerId, Consumer<ServiceAnswer> onAnswer) {
        var playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            var player = new Player();
            player.setId(playerId);
            player.setState(Player.State.DEFAULT);
            playerRepository.save(player);

            onAnswer.accept(new ServiceAnswer("Добро пожаловать! Теперь вы зарагистрированы. Ознакомьтесь с правилами по команде /help.", playerId, null));
            return;
        }

        onAnswer.accept(new ServiceAnswer("Вы уже зарегестрированы.", playerId, null));
    }
}

package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component("/ping")
public class PingCommandHandler implements CommandHandler {
    @Override
    public void handle(Long playerId, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(new ServiceAnswer("`pong`", playerId, null));
    }
}

package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;

import java.util.function.Consumer;

public class PingCommandHandler implements CommandHandler {
    @Override
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(new ServiceAnswer("```pong```", playerId, null));
    }
}

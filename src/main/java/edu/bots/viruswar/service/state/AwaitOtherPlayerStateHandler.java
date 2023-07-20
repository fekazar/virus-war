package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.ServiceAnswer;

import java.util.function.Consumer;

public class AwaitOtherPlayerStateHandler implements StateHandler {
    @Override
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(new ServiceAnswer("It's not your turn.", playerId, null));
    }
}

package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.ServiceAnswer;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class AwaitOtherPlayerStateHandler implements StateHandler {
    @Override
    public void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(new ServiceAnswer("Это не ваш ход.", playerId, null));
    }
}

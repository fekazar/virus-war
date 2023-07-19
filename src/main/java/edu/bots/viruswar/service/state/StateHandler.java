package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.ServiceAnswer;

import java.util.function.Consumer;

public interface StateHandler {
    void handle(Long playerId, String msg, Consumer<ServiceAnswer> onAnswer);
}

package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;

import java.util.function.Consumer;

public interface CommandHandler {
    void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer);
}

package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;

public interface CommandHandler {
    ServiceAnswer handle(Long userId, String command);
}
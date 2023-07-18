package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.ServiceAnswer;

public class PingCommandHandler implements CommandHandler {
    @Override
    public ServiceAnswer handle(Long playerId, String command) {
        return new ServiceAnswer("pong", false);
    }
}

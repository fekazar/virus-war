package edu.bots.viruswar.service.state;

import edu.bots.viruswar.model.ServiceAnswer;

public interface StateHandler {
    ServiceAnswer handle(Long playerId, String msg);
}

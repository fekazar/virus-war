package edu.bots.viruswar.service.command;

import com.pengrad.telegrambot.model.request.ForceReply;
import edu.bots.viruswar.model.ServiceAnswer;

public class DeleteCommandHandler implements CommandHandler {

    @Override
    public ServiceAnswer handle(Long playerId, String command) {
        return new ServiceAnswer("Enter session id:", new ForceReply());
    }
}

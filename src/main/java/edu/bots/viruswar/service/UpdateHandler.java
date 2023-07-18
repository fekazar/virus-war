package edu.bots.viruswar.service;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.service.command.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
public class UpdateHandler {
    private final Map<String, CommandHandler> commandHandlers;

    public UpdateHandler(Map<String, CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    public void handle(Update update, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(handleCommand(update).get());
        //onAnswer.accept(new ServiceAnswer("Not implemented yet.", false));
    }

    private Optional<ServiceAnswer> handleCommand(Update update) {
        if (update.message() == null)
            return Optional.empty();

        var commandOpt = Arrays.stream(update.message().entities())
                .filter(messageEntity -> messageEntity.type().equals(MessageEntity.Type.bot_command))
                .findAny();

        if (commandOpt.isEmpty())
            return Optional.empty();

        var comEntity = commandOpt.get();

        var msg = update.message().text();
        var com = msg.substring(comEntity.offset(), comEntity.length());

        var handler = commandHandlers.get(com);

        if (handler == null) {
            log.info("No handler found for " + com);
            return Optional.empty();
        }

        return Optional.ofNullable(handler.handle(update.message().from().id(), com));
    }
}

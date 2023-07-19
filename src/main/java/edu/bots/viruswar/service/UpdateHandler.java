package edu.bots.viruswar.service;

import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.model.ServiceAnswer;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.service.command.CommandHandler;
import edu.bots.viruswar.service.state.StateHandler;
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
    private final Map<Player.State, StateHandler> stateHandlers;
    private final PlayerRepository playerRepository;

    public UpdateHandler(Map<String, CommandHandler> commandHandlers, Map<Player.State, StateHandler> stateHandlers, PlayerRepository playerRepository) {
        this.commandHandlers = commandHandlers;
        this.stateHandlers = stateHandlers;
        this.playerRepository = playerRepository;
    }

    public void handle(Update update, Consumer<ServiceAnswer> onAnswer) {
        if (update.message() == null) {
            return;
        }

        var msg = update.message().text().trim();

        Optional<MessageEntity> commandOpt = update.message().entities() == null ? Optional.empty() : Arrays.stream(update.message().entities())
                .filter(messageEntity -> messageEntity.type().equals(MessageEntity.Type.bot_command))
                .findAny();

        if (commandOpt.isPresent()) {
            var comEntity = commandOpt.get();
            var com = msg.substring(comEntity.offset(), comEntity.length());

            var handler = commandHandlers.get(com);

            if (handler == null) {
                log.info("No command handler found for " + com);
            } else {
                handler.handle(update.message().from().id(), msg, onAnswer);
                return;
            }
        } else {
            log.info("No command present.");
        }

        var state = playerRepository.getState(update.message().from().id());
        log.info("state: " + state);
        var stateHandler = stateHandlers.get(state);

        if (stateHandler == null) {
            log.info("No state handler found for " + state);
            throw new RuntimeException("No handlers found for this action.");
        } else {
            log.info("Found handler for " + state);
            stateHandler.handle(update.message().from().id(), msg, onAnswer);
        }
    }
}

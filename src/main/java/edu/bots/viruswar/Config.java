package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bots.viruswar.game.GameUtils;
import edu.bots.viruswar.model.Player;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import edu.bots.viruswar.service.UpdateHandler;
import edu.bots.viruswar.service.command.*;
import edu.bots.viruswar.service.state.AwaitConnectIdStateHandler;
import edu.bots.viruswar.service.state.AwaitCoordinatesStateHandler;
import edu.bots.viruswar.service.state.AwaitOtherPlayerStateHandler;
import edu.bots.viruswar.service.state.StateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Config {
    @Bean
    public TelegramBot getBot(@Value("${bot-token}") String botApiToken, UpdateHandler updateHandler) {
        var bot = new TelegramBot(botApiToken);

        bot.setUpdatesListener(updates -> {
            for (var update: updates) {
                updateHandler.handle(update, serviceAnswer -> {
                    var msg = new SendMessage(serviceAnswer.sendTo(), serviceAnswer.message());
                    if (serviceAnswer.reply() != null)
                        msg.replyMarkup(serviceAnswer.reply());

                    msg.parseMode(ParseMode.Markdown);
                    bot.execute(msg);
                });
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return bot;
    }

    @Bean
    public Map<String, CommandHandler> getCommandHandlers(PlayerRepository playerRepository,
                                                          SessionRepository sessionRepository,
                                                          GameUtils gameUtils) {
        return Map.of(
                "/ping", new PingCommandHandler(),
                "/start", new StartCommandHandler(playerRepository),
                "/create", new CreateCommandHandler(sessionRepository, gameUtils),
                "/disconnect", new DisconnectCommandHandler(playerRepository, sessionRepository),
                "/connect", new ConnectCommandHandler(playerRepository, sessionRepository)
        );
    }

    @Bean
    public Map<Player.State, StateHandler> getStateHandlers(PlayerRepository playerRepository,
                                                            SessionRepository sessionRepository,
                                                            GameUtils gameUtils) {
        return Map.of(
                Player.State.AWAITS_CONNECT_ID, new AwaitConnectIdStateHandler(playerRepository, sessionRepository, gameUtils),
                Player.State.AWAITS_COORDINATES, new AwaitCoordinatesStateHandler(playerRepository, sessionRepository, gameUtils),
                Player.State.AWAITS_OTHER_PLAYER, new AwaitOtherPlayerStateHandler()
        );
    }

    @Bean
    public GameUtils getGameUtils() {
        return new GameUtils(10, 10, 3);
    }

    @Bean
    public ExecutorService updateHandlerThreadPool() {
        return Executors.newCachedThreadPool();
    }
}

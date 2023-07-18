package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import edu.bots.viruswar.repository.PlayerRepository;
import edu.bots.viruswar.repository.SessionRepository;
import edu.bots.viruswar.service.command.CommandHandler;
import edu.bots.viruswar.service.command.CreateCommandHandler;
import edu.bots.viruswar.service.command.PingCommandHandler;
import edu.bots.viruswar.service.command.StartCommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class Config {
    @Bean
    public TelegramBot getBot(@Value("${bot-token}") String botApiToken) {
        return new TelegramBot(botApiToken);
    }

    @Bean
    public Map<String, CommandHandler> getCommandHandlers(PlayerRepository playerRepository,
                                                          SessionRepository sessionRepository) {
        return Map.of(
                "/ping", new PingCommandHandler(),
                "/start", new StartCommandHandler(playerRepository),
                "/create", new CreateCommandHandler(sessionRepository)
        );
    }
}

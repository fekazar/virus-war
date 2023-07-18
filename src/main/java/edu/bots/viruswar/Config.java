package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import edu.bots.viruswar.service.command.CommandHandler;
import edu.bots.viruswar.service.command.PingCommandHandler;
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
    public Map<String, CommandHandler> getCpmmandHandlers() {
        return Map.of(
                "/ping", new PingCommandHandler()
        );
    }
}

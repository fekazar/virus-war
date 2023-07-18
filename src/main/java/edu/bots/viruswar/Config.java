package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public TelegramBot getBot(@Value("${bot-token}") String botApiToken) {
        return new TelegramBot(botApiToken);
    }
}

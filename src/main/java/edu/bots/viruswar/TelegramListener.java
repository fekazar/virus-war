package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bots.viruswar.service.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramListener {
    private final TelegramBot bot;
    private final UpdateHandler updateHandler;

    public TelegramListener(TelegramBot bot, UpdateHandler updateHandler) {
        this.bot = bot;
        this.updateHandler = updateHandler;

        this.bot.setUpdatesListener(updates -> {
            for (var update: updates) {
                log.info(String.valueOf(update));

                try {
                    updateHandler.handle(update, serviceAnswer -> {
                        bot.execute(new SendMessage(update.message().chat().id(), serviceAnswer.message()));
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                    bot.execute(new SendMessage(update.message().chat().id(), "Internal error"));
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}

package edu.bots.viruswar;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import edu.bots.viruswar.service.UpdateHandler;
import org.springframework.stereotype.Component;

@Component
public class TelegramListener {
    private final TelegramBot bot;
    private final UpdateHandler updateHandler;

    public TelegramListener(TelegramBot bot, UpdateHandler updateHandler) {
        this.bot = bot;
        this.updateHandler = updateHandler;

        this.bot.setUpdatesListener(updates -> {
            for (var update: updates) {
                updateHandler.handle(update, msg -> {
                    bot.execute(new SendMessage(update.message().chat().id(), msg));
                });
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}

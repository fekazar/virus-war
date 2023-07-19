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
                try {
                    updateHandler.handle(update, serviceAnswer -> {
                        var msg = new SendMessage(serviceAnswer.sendTo(), serviceAnswer.message());
                        if (serviceAnswer.reply() != null)
                            msg.replyMarkup(serviceAnswer.reply());

                        bot.execute(msg);
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                    bot.execute(new SendMessage(update.message().from().id(), "Internal error"));
                }
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}

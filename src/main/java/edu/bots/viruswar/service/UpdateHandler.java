package edu.bots.viruswar.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UpdateHandler {
    public void handle(Update update, Consumer<SendMessage> onAnswer) {
        onAnswer.accept(new SendMessage(update.message().chat().id(), "Not implemented yet."));
    }
}

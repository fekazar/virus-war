package edu.bots.viruswar.service;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UpdateHandler {
    public void handle(Update update, Consumer<String> onAnswer) {
        onAnswer.accept("Not implemented yet.");
    }
}

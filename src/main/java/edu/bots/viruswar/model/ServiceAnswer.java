package edu.bots.viruswar.model;

import com.pengrad.telegrambot.model.request.Keyboard;

public record ServiceAnswer(String message, Keyboard reply) {
}

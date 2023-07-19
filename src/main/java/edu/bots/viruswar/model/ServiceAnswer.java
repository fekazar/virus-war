package edu.bots.viruswar.model;

import com.pengrad.telegrambot.model.request.Keyboard;

public record ServiceAnswer(String message, Long sendTo, Keyboard reply) {
}

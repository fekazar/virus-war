package edu.bots.viruswar.service.command;

import edu.bots.viruswar.model.Figure;
import edu.bots.viruswar.model.ServiceAnswer;

import java.util.function.Consumer;

public class HelpCommandHandler implements CommandHandler {
    @Override
    public void handle(Long playerId, String command, Consumer<ServiceAnswer> onAnswer) {
        onAnswer.accept(new ServiceAnswer("""
                [Правила](https://ru.wikipedia.org/wiki/%D0%92%D0%BE%D0%B9%D0%BD%D0%B0_%D0%B2%D0%B8%D1%80%D1%83%D1%81%D0%BE%D0%B2)
                
                Игрок, создавший сессию, ходит первым (крестиками), подключившийся -- вторым, ноликами.
                
                ***Обозначения***
                """
                + "```\n"
                + Figure.EMPTY + " - пустая клета\n"
                + Figure.CROSS + " - живой крестик\n"
                + Figure.CIRCLE + " - живой нолик\n"
                + Figure.KILLED_CROSS  + " - убитый крестик\n"
                + Figure.KILLED_CIRCLE + " - убитый нолик\n"
                + "```", playerId, null));
    }
}

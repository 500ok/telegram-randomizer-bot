package org.randomizer.bot;

import org.randomizer.model.Game;
import org.randomizer.randomizer.GameRandomizer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.Callable;

public class CommandExecutor {

    private static final GameRandomizer randomizerService = new GameRandomizer();

    private final Message message;

    public CommandExecutor(Message message) {
        this.message = message;
    }

    public SendMessage execute() {
        if (message.getText().equalsIgnoreCase("/game")) {
            Game game = randomizerService.getRandomGame();

            if (game == null) return execute();

            return SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(game.toString())
                    .build();
        }

        return SendMessage.builder()
                .text("no such command")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }
}

package org.randomizer.bot;

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

    //todo
    public SendMessage execute() {
        if (message.getText().equalsIgnoreCase("/game")) {
            String text = randomizerService.getRandomGame()
                    .toString();

            return SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(text)
                    .build();
        }

        return SendMessage.builder()
                .text("no such command")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }
}

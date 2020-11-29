package org.randomizer.bot;

import org.randomizer.model.Game;
import org.randomizer.randomizer.GameRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandExecutor {

    private static final GameRandomizer randomizerService = new GameRandomizer();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private final Message message;

    public CommandExecutor(Message message) {
        this.message = message;
    }

    public SendMessage execute() {
        SendMessage sendMessage;
        if (message.getText().equalsIgnoreCase("/game")) {
            Game game = randomizerService.getRandomGame();

            if (game == null) return execute();

             sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(game.format())
                    .build();

             LOGGER.debug("Message {} was sent with length {}",
                     message.getChatId(), sendMessage.getText().length());
             return sendMessage;
        }

        return SendMessage.builder()
                .text("Command does not exists.")
                .chatId(String.valueOf(message.getChatId()))
                .build();
    }
}

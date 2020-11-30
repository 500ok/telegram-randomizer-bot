package org.randomizer.bot;

import org.randomizer.model.FormattedMessage;
import org.randomizer.model.Game;
import org.randomizer.model.Movie;
import org.randomizer.randomizer.GameRandomizer;
import org.randomizer.randomizer.MovieRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class CommandExecutor {

    private static final GameRandomizer gameRandomizer = new GameRandomizer();
    private static final MovieRandomizer movieRandomizer = new MovieRandomizer();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private final Message message;

    public CommandExecutor(Message message) {
        this.message = message;
    }

    public SendMessage execute() {
        SendMessage sendMessage;
        String command = message.getText();
        String chatId = String.valueOf(message.getChatId());
        FormattedMessage formattedMessage = null;

        if (command.equalsIgnoreCase("/game")) {
            Game game = gameRandomizer.getRandomGame();

            if (game != null) {
                formattedMessage = game;
            }
        } else if (command.equalsIgnoreCase("/movie")) {
            Movie movie = movieRandomizer.getRandomMovie();

            if (movie != null) {
                formattedMessage = movie;
            }
        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Try again.")
                    .build();
        }

        if (formattedMessage != null) {
            sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(formattedMessage.getFormattedMessage())
                    .build();

            LOGGER.debug("Message {} was sent with length {}",
                    message.getChatId(), sendMessage.getText().length());

            return sendMessage;
        }

        return SendMessage.builder()
                .text("Command does not exists.")
                .chatId(chatId)
                .build();
    }
}

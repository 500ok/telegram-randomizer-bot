package org.randomizer.bot;

import lombok.extern.slf4j.Slf4j;
import org.randomizer.bot.formatter.MessageFormatter;
import org.randomizer.bot.menu.MenuState;
import org.randomizer.bot.menu.MenuView;
import org.randomizer.model.Game;
import org.randomizer.model.Movie;
import org.randomizer.randomizer.GameRandomizer;
import org.randomizer.randomizer.MovieRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Slf4j
@Component
public class MessageHandler {

    @Autowired
    private MessageFormatter<Game> gameMessageFormatter;
    @Autowired
    private MessageFormatter<Movie> movieMessageFormatter;
    @Autowired
    private GameRandomizer gameRandomizer;
    @Autowired
    private MovieRandomizer movieRandomizer;

    private final Map<MenuState ,MenuView> menus;

    @Autowired
    public MessageHandler(Map<MenuState, MenuView> menus) {
        this.menus = menus;
    }

    public BotApiMethod<?> handle(Message message) {
        BotApiMethod<?> sendMessage;
        String messageText;
        String command = message.getText();
        String chatId = String.valueOf(message.getChatId());

        log.debug("Received command \"{}\" by {}", message.getText(), chatId);

        switch (command){
            case "/game":
                messageText = gameMessageFormatter.getFormattedMessage(gameRandomizer.getRandomGame());
                break;
            case "/movie":
                messageText = movieMessageFormatter.getFormattedMessage(movieRandomizer.getRandomMovie());
                break;
            default:
                return SendMessage.builder()
                        .text("Command does not exists.")
                        .chatId(chatId)
                        .build();
        }

        if (messageText != null) {
            sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(messageText)
                    .build();

            log.debug("Message {} was sent.", message.getChatId());

            return sendMessage;
        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Try again.")
                    .build();
        }
    }
}

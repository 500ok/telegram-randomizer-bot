package org.randomizer.bot;

import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.FormattedMessage;
import org.randomizer.randomizer.GameRandomizer;
import org.randomizer.randomizer.MovieRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class CommandExecutor {

    @Autowired
    private GameRandomizer gameRandomizer;
    @Autowired
    private MovieRandomizer movieRandomizer;

    public SendMessage execute(Message message) {
        SendMessage sendMessage;
        FormattedMessage formattedMessage;
        String command = message.getText();
        String chatId = String.valueOf(message.getChatId());

        log.debug("Received command \"{}\" by {}", message.getText(), chatId);

        switch (command){
            case "/game":
                formattedMessage = gameRandomizer.getRandomGame();
                break;
            case "/movie":
                formattedMessage = movieRandomizer.getRandomMovie();
                break;
            default:
                return SendMessage.builder()
                        .text("Command does not exists.")
                        .chatId(chatId)
                        .build();
        }

        if (formattedMessage != null) {
            sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(message.getChatId()))
                    .parseMode("markdown")
                    .text(formattedMessage.getFormattedMessage())
                    .build();

            log.debug("Message {} was sent with length {}",
                    message.getChatId(), sendMessage.getText().length());

            return sendMessage;
        } else {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Try again.")
                    .build();
        }
    }
}

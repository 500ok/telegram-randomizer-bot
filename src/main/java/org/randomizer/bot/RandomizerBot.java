package org.randomizer.bot;

import org.randomizer.config.Config;
import org.randomizer.randomizer.GameRandomizer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RandomizerBot extends TelegramLongPollingBot {

    private final String name = Config.getProperty("bot.name");
    private final String token = Config.getProperty("bot.token");
    private final GameRandomizer randomizerService = new GameRandomizer();

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(SendMessage.builder()
                        .chatId(String.valueOf(update.getMessage().getChatId()))
                        .text(
                            randomizerService.getRandomGame()
                                    .toString()
                                    .replaceAll("</?\\w+>", "")
                        ).build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}

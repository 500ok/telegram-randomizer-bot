package org.randomizer;

import org.randomizer.bot.RandomizerBot;
import org.randomizer.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            Config.load("bot.properties");
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new RandomizerBot());
        } catch (TelegramApiException | IOException e) {
            LOGGER.error("Fatal error: {}", e.toString());
        }
    }
}

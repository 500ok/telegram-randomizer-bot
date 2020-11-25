package org.randomizer;

import org.randomizer.bot.RandomizerBot;
import org.randomizer.config.Config;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Config.load("bot.properties");
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new RandomizerBot());
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }
}

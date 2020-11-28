package org.randomizer.bot;

import kong.unirest.Unirest;
import org.randomizer.config.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomizerBot extends TelegramLongPollingBot {

    private static final String name = Config.getProperty("bot.name");
    private static final String token = Config.getProperty("bot.token");
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

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
            CompletableFuture.supplyAsync(
                    () -> new CommandExecutor(update.getMessage()).execute(),
                    executor
            ).thenAccept(
                    (message) -> {
                        try {
                            executeAsync(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
            );
    }

    @Override
    public void onClosing() {
        super.onClosing();
        Unirest.shutDown();
    }
}

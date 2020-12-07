package org.randomizer.bot;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class RandomizerBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String name;
    @Autowired
    private CommandExecutor commandExecutor;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
                () -> commandExecutor.execute(update.getMessage()), executor
        ).thenAcceptAsync(
            (message) -> {
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error("Bot error {}: {}",
                            message.getChatId(),
                            e.toString()
                    );
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
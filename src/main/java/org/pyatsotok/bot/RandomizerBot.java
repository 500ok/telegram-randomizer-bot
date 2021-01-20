package org.pyatsotok.bot;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RandomizerBot extends TelegramWebhookBot {
    @Value("${bot.path}")
    private String path;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.name}")
    private String name;

    @Autowired
    private UpdateDispatcher updateDispatcher;

    public RandomizerBot(@Autowired DefaultBotOptions options) {
        super(options);
    }

    @PostConstruct
    private void init() {
        log.debug("Register {} bot", name);
        HttpResponse<JsonNode> request = Unirest.post(
                    String.format("https://api.telegram.org/bot%s/setWebhook?url=%s",
                            getBotToken(), getBotPath())).asJson();
        log.debug("Bot {} was registered", name);
    }

    @Override
    public String getBotPath() {
        return path;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateDispatcher.dispatch(update);
    }
}
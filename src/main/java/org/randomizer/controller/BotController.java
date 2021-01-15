package org.randomizer.controller;

import lombok.extern.slf4j.Slf4j;
import org.randomizer.bot.RandomizerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
public class BotController {

    private final RandomizerBot randomizerBot;

    @Autowired
    public BotController(RandomizerBot randomizerBot) {
        this.randomizerBot = randomizerBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> update(@RequestBody Update update) {
        log.debug(update.toString());
        BotApiMethod<?> response = randomizerBot.onWebhookUpdateReceived(update);
        log.debug(response.toString());
        return response;
    }
}

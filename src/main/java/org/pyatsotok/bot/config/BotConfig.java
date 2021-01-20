package org.pyatsotok.bot.config;

import org.pyatsotok.bot.menu.BotState;
import org.pyatsotok.bot.menu.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class BotConfig {

    @Bean
    public DefaultBotOptions botConfiguration() {
        DefaultBotOptions options = new DefaultBotOptions();

        options.setMaxWebhookConnections(10);
        options.setMaxThreads(Runtime.getRuntime().availableProcessors());

        return options;
    }

    @Bean
    public Map<BotState, MessageHandler> handlers(Collection<MessageHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(MessageHandler::getState, Function.identity()));
    }

}

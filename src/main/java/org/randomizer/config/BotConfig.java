package org.randomizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class BotConfig {

    @Bean
    public DefaultBotOptions randomizerBotConfiguration() {
        DefaultBotOptions options = new DefaultBotOptions();

        options.setMaxWebhookConnections(10);
        options.setMaxThreads(Runtime.getRuntime().availableProcessors());

        return options;
    }

}

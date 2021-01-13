package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class MovieMenuHandler implements CallbackQueryHandler {
    @Override
    public BotApiMethod<?> handle(UserData userData, CallbackQuery query) {

        return null;
    }

    @Override
    public BotState getState() {
        return null;
    }
}

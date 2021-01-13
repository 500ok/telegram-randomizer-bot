package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class MainMenuHandler implements CallbackQueryHandler {
    @Override
    public BotApiMethod<?> handle(UserData userData, CallbackQuery query) {
        return null;
    }

    @Override
    public BotState getState() {
        return BotState.MAIN;
    }
}

package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackQueryHandler {

     BotApiMethod<?> handle(UserData userData, CallbackQuery query);
     BotState getState();

}

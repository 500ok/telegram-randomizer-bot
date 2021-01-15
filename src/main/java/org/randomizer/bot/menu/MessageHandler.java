package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface MessageHandler {

     BotApiMethod<?> handle(UserData userData, String messageData);
     BotState getState();

}

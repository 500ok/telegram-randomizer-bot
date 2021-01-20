package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.domain.UserData;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface MessageHandler {

     BotApiMethod<?> handle(UserData userData, String messageData);
     BotState getState();


}

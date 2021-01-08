package org.randomizer.bot.menu;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MenuView {
     BotApiMethod<?> getView(Message message);
     MenuState getState();
}

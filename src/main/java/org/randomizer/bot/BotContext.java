package org.randomizer.bot;

import org.randomizer.bot.menu.BotState;
import org.randomizer.bot.menu.CallbackQueryHandler;
import org.randomizer.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotContext {

    private final Map<Long, UserData> userDataMap = new HashMap<>();
    private final Map<BotState, CallbackQueryHandler> handlers;

    @Autowired
    public BotContext(Map<BotState, CallbackQueryHandler> handlers) {
        this.handlers = handlers;
    }

    public CallbackQueryHandler getHandlerByState(BotState state) {
        return handlers.get(state);
    }

    public UserData getUserData(Long id) {
        UserData data = userDataMap.get(id);

        if (data == null)
            return userDataMap.put(id, new UserData(id, BotState.MAIN));
        else
            return data;
    }
}

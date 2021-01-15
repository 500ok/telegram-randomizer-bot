package org.randomizer.bot;

import org.randomizer.bot.menu.BotState;
import org.randomizer.bot.menu.MessageHandler;
import org.randomizer.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotContext {

    private final Map<Long, UserData> userDataMap = new HashMap<>();
    private final Map<BotState, MessageHandler> handlers;

    @Autowired
    public BotContext(Map<BotState, MessageHandler> handlers) {
        this.handlers = handlers;
    }

    public MessageHandler getHandlerByState(BotState state) {
        return handlers.get(state);
    }

    public UserData getUserData(Long id) {
        UserData data = userDataMap.get(id);

        if (data == null){
            data = new UserData(id, BotState.MAIN);
            userDataMap.put(id, data);
        }

        return data;
    }
}

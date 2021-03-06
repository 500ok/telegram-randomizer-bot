package org.pyatsotok.bot;

import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.bot.menu.BotState;
import org.pyatsotok.bot.menu.MessageHandler;
import org.pyatsotok.bot.domain.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Slf4j
@Component
public class UpdateDispatcher {

    private final BotContext context;

    @Autowired
    public UpdateDispatcher(BotContext context) {
        this.context = context;
    }

    public BotApiMethod<?> dispatch(Update update) {

        Long userId;
        Integer messageId;

        if (update.hasMessage()) {
            userId = update.getMessage().getChatId();
            messageId = update.getMessage().getMessageId();
        } else {
            userId = update.getCallbackQuery().getMessage().getChatId();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        }

        UserData data = context.getUserData(userId);

        BotApiMethod<?> message;
        MessageHandler handler;
  
        if (update.hasCallbackQuery()) {
            data.setMenuId(messageId);
            CallbackQuery query = update.getCallbackQuery();
            String[] queryParam = query.getData().split(":");
            log.debug("Received callback query \"{}\" from {}", Arrays.toString(queryParam), userId);

            BotState actionState = BotState.valueOf(queryParam[0]);
            if (actionState == BotState.NEW) {
                handler = context.getHandlerByState(BotState.MAIN);
                message = handler.handle(data, null);
            } else {
                handler = context.getHandlerByState(actionState);

                if (handler == null)
                    handler = context.getHandlerByState(data.getState());

                message = handler.handle(data, queryParam.length > 1? queryParam[1]: null);
            }
        } else {
            data.setState(BotState.NEW);
            handler = context.getHandlerByState(BotState.MAIN);
            message = handler.handle(data, null);
        }

        return message;
    }
}

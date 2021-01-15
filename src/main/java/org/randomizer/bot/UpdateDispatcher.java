package org.randomizer.bot;

import lombok.extern.slf4j.Slf4j;
import org.randomizer.bot.formatter.MessageFormatter;
import org.randomizer.bot.menu.BotState;
import org.randomizer.bot.menu.MessageHandler;
import org.randomizer.model.Game;
import org.randomizer.model.Movie;
import org.randomizer.model.UserData;
import org.randomizer.randomizer.GameRandomizer;
import org.randomizer.randomizer.MovieRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

@Slf4j
@Component
public class UpdateDispatcher {

    @Autowired
    private MessageFormatter<Game> gameMessageFormatter;
    @Autowired
    private MessageFormatter<Movie> movieMessageFormatter;
    @Autowired
    private GameRandomizer gameRandomizer;
    @Autowired
    private MovieRandomizer movieRandomizer;

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
            handler = context.getHandlerByState(BotState.valueOf(queryParam[0]));
            if (handler == null)
                handler = context.getHandlerByState(data.getState());

            message = handler.handle(data, queryParam.length > 1? queryParam[1]: null);
        } else {
            data.setState(BotState.MAIN);
            handler = context.getHandlerByState(BotState.MAIN);
            message = handler.handle(data, null);
        }

        return message;
    }
}

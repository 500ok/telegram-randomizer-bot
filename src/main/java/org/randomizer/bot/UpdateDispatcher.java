package org.randomizer.bot;

import lombok.extern.slf4j.Slf4j;
import org.randomizer.bot.formatter.MessageFormatter;
import org.randomizer.bot.menu.BotState;
import org.randomizer.model.Game;
import org.randomizer.model.Movie;
import org.randomizer.model.UserData;
import org.randomizer.randomizer.GameRandomizer;
import org.randomizer.randomizer.MovieRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        Message inputMessage = update.getMessage();
        Long userId = inputMessage.getChatId();
        UserData data = context.getUserData(userId);

        BotApiMethod<?> message;

        if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            log.debug("Received callback query \"{}\" from {}", query.getData(), data.getId());
            message = context.getHandlerByState(data.getState()).handle(data, query);
        } else {
            data.setState(BotState.MAIN);
            message = context.getHandlerByState(data.getState()).handle(data, null);
        }

        return message;
    }
}

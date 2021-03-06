package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.formatter.MessageFormatter;
import org.pyatsotok.randomizer.domain.Movie;
import org.pyatsotok.bot.domain.UserData;
import org.pyatsotok.randomizer.randomizer.MovieRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMenuHandler implements MessageHandler {

    private static final List<BotState> menuOptions = List.of(
            BotState.MOVIE_GENRE_FILTER
    );

    private final MessageFormatter<Movie> movieMessageFormatter;

    private final MovieRandomizer movieRandomizer;


    @Autowired
    public MovieMenuHandler(MessageFormatter<Movie> movieMessageFormatter, MovieRandomizer movieRandomizer) {
        this.movieMessageFormatter = movieMessageFormatter;
        this.movieRandomizer = movieRandomizer;
    }

    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        userData.setState(getState());

        if (messageData != null && !messageData.isBlank()) {
            if ("generate".equals(messageData)) {
                return updateMovieMessage(userData);
            }
        }

        return updateMenu(userData);
    }

    private BotApiMethod<?> updateMovieMessage(UserData data) {
        EditMessageText editMessageText = new EditMessageText();

        String message = "Try again.";
        Movie movie;

        if (data.getMovieFilter().isEmpty()) {
            movie = movieRandomizer.getRandomMovie();
        } else {
            movie = movieRandomizer.getRandomMovie(data.getMovieFilter());
            data.getMovieFilter().clear();
        }

        if (movie != null)
            message = movieMessageFormatter.getFormattedMessage(movie);

        editMessageText.setParseMode(ParseMode.MARKDOWN);
        editMessageText.setText(message);

        InlineKeyboardButton toMenu = new InlineKeyboardButton();
        toMenu.setText(BotState.MAIN.getDescription());
        toMenu.setCallbackData(BotState.NEW.name());
        editMessageText.setMessageId(data.getMenuId());
        editMessageText.setChatId(data.getId().toString());

        return editMessageText;
    }

    private BotApiMethod<?> updateMenu(UserData data) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(createKeyboard());
        editMessageReplyMarkup.setMessageId(data.getMenuId());
        editMessageReplyMarkup.setChatId(data.getId().toString());
        return editMessageReplyMarkup;
    }

    private InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        for (BotState menu: menuOptions) {
            InlineKeyboardButton movieFilterButton = new InlineKeyboardButton();
            movieFilterButton.setText(menu.getDescription());
            movieFilterButton.setCallbackData(menu.name());
            keyboardRows.add(List.of(movieFilterButton));
        }


        InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
        backToMenuButton.setText("Back to " + BotState.MAIN.getDescription());
        backToMenuButton.setCallbackData(BotState.MAIN.name());

        InlineKeyboardButton requestGameButton = new InlineKeyboardButton();
        requestGameButton.setText("Get movie");
        requestGameButton.setCallbackData(getState() + ":" + "generate");

        keyboardRows.add(List.of(backToMenuButton, requestGameButton));

        markup.setKeyboard(keyboardRows);
        return markup;
    }

    @Override
    public BotState getState() {
        return BotState.MOVIE_MAIN;
    }
}

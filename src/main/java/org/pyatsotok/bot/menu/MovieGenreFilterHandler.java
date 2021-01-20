package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.domain.UserData;
import org.pyatsotok.randomizer.domain.MovieGenre;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MovieGenreFilterHandler implements MessageHandler {

    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        userData.setState(getState());

        if (messageData != null && !messageData.isBlank()) {
            fillUserFilter(userData, messageData);
        }

        return updateMenu(userData);
    }

    private void fillUserFilter(UserData data, String genreParam) {
        Set<MovieGenre> genres = data.getMovieFilter().getGenres();
        MovieGenre genre = MovieGenre.valueOf(genreParam);

        if (!genres.add(genre)) {
            genres.remove(genre);
        }
    }

    private BotApiMethod<?> updateMenu(UserData data) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(createKeyboard(data));
        editMessageReplyMarkup.setMessageId(data.getMenuId());
        editMessageReplyMarkup.setChatId(data.getId().toString());
        return editMessageReplyMarkup;
    }

    private InlineKeyboardMarkup createKeyboard(UserData data) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        Set<MovieGenre> genres = data.getMovieFilter().getGenres();

        for (MovieGenre genreOption: MovieGenre.values()) {
            InlineKeyboardButton genreFilterButton = new InlineKeyboardButton();
            genreFilterButton.setText(genreOption.getTitle());
            genreFilterButton.setCallbackData(getState().name() + ":" + genreOption.name());

            if (genres.contains(genreOption)) {
                InlineKeyboardButton selectedMark = new InlineKeyboardButton();
                selectedMark.setText("<-");
                selectedMark.setCallbackData(getState().name() + ":" + genreOption.name());

                keyboardRows.add(List.of(genreFilterButton, selectedMark));
            } else {
                keyboardRows.add(List.of(genreFilterButton));
            }
        }


        InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
        backToMenuButton.setText("Back to " + BotState.MOVIE_MAIN.getDescription());
        backToMenuButton.setCallbackData(BotState.MOVIE_MAIN.name());

        keyboardRows.add(List.of(backToMenuButton));

        markup.setKeyboard(keyboardRows);
        return markup;
    }

    @Override
    public BotState getState() {
        return BotState.MOVIE_GENRE_FILTER;
    }
}

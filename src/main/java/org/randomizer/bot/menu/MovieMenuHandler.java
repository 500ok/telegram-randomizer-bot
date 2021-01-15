package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMenuHandler implements MessageHandler {
    private final List<BotState> menuOptions = List.of(
            BotState.MOVIE_GENRE_FILTER
    );

    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        return updateMenu(userData);
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

            InlineKeyboardButton gameFilterButton = new InlineKeyboardButton();
            gameFilterButton.setText(menu.name());
            gameFilterButton.setCallbackData(getState() + ":" + BotState.MOVIE_MAIN.name());
            keyboardRows.add(List.of(gameFilterButton));
        }


        InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
        backToMenuButton.setText("Back to menu");
        backToMenuButton.setCallbackData(BotState.MAIN.name() + ":" + "old");

        InlineKeyboardButton requestGameButton = new InlineKeyboardButton();
        requestGameButton.setText("Get movie");
        requestGameButton.setCallbackData(BotState.MOVIE_MAIN.name());

        keyboardRows.add(List.of(backToMenuButton, requestGameButton));

        markup.setKeyboard(keyboardRows);
        return markup;
    }

    @Override
    public BotState getState() {
        return BotState.MOVIE_MAIN;
    }
}

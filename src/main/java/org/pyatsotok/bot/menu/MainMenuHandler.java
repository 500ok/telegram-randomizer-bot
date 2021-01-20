package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.domain.UserData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainMenuHandler implements MessageHandler {

    private static final List<BotState> menuOptions = List.of(
            BotState.GAME_MAIN,
            BotState.MOVIE_MAIN
    );

    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {

        if (userData.getState() == BotState.NEW) {
            return createMainMenu(userData);
        }

        userData.setState(getState());

        return updateToMainMenu(userData);
    }

    private BotApiMethod<?> updateToMainMenu(UserData data) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(createKeyboard());
        editMessageReplyMarkup.setMessageId(data.getMenuId());
        editMessageReplyMarkup.setChatId(data.getId().toString());
        return editMessageReplyMarkup;
    }

    private BotApiMethod<?> createMainMenu(UserData data) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(createKeyboard());
        message.setChatId(data.getId().toString());
        message.setText("MENU");
        message.setParseMode("HTML");
        return message;
    }

    private InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        for (BotState menu: menuOptions) {

            InlineKeyboardButton menuButton = new InlineKeyboardButton();
            menuButton.setText(menu.getDescription());
            menuButton.setCallbackData(menu.name());
            keyboardRow.add(menuButton);
        }

        keyboardRows.add(keyboardRow);
        markup.setKeyboard(keyboardRows);
        return markup;
    }

    @Override
    public BotState getState() {
        return BotState.MAIN;
    }
}

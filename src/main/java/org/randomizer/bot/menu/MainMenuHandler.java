package org.randomizer.bot.menu;

import org.randomizer.model.UserData;
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

    private final List<BotState> menuOptions = List.of(BotState.GAME_MAIN, BotState.MOVIE_MAIN);

    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        if (messageData == null)
            return createMainMenu(userData.getId());

        return updateToMainMenu(userData);
    }

    private BotApiMethod<?> updateToMainMenu(UserData data) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(createKeyboard());
        editMessageReplyMarkup.setMessageId(data.getMenuId());
        editMessageReplyMarkup.setChatId(data.getId().toString());
        return editMessageReplyMarkup;
    }

    private BotApiMethod<?> createMainMenu(Long userId) {
        SendMessage message = new SendMessage();
        message.setReplyMarkup(createKeyboard());
        message.setChatId(userId.toString());
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
            menuButton.setText(menu.name());
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

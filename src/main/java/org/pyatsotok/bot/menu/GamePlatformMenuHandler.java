package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.domain.UserData;
import org.pyatsotok.randomizer.domain.GamePlatform;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GamePlatformMenuHandler implements MessageHandler{
    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        userData.setState(getState());

        if (messageData != null && !messageData.isBlank()) {
            fillUserFilter(userData, messageData);
        }

        return updateMenu(userData);
    }

    private void fillUserFilter(UserData data, String platformParam) {
        Set<GamePlatform> platforms = data.getGameFilter().getPlatforms();
        GamePlatform platform = GamePlatform.valueOf(platformParam);

        if (!platforms.add(platform)) {
            platforms.remove(platform);
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
        Set<GamePlatform> platforms = data.getGameFilter().getPlatforms();

        for (GamePlatform platformOption: GamePlatform.values()) {
            InlineKeyboardButton genreFilterButton = new InlineKeyboardButton();
            genreFilterButton.setText(platformOption.getTitle());
            genreFilterButton.setCallbackData(getState().name() + ":" + platformOption.name());

            if (platforms.contains(platformOption)) {
                InlineKeyboardButton selectedMark = new InlineKeyboardButton();
                selectedMark.setText("<-");
                selectedMark.setCallbackData(getState().name() + ":" + platformOption.name());

                keyboardRows.add(List.of(genreFilterButton, selectedMark));
            } else {
                keyboardRows.add(List.of(genreFilterButton));
            }
        }


        InlineKeyboardButton backToMenuButton = new InlineKeyboardButton();
        backToMenuButton.setText("Back to " + BotState.GAME_MAIN.getDescription());
        backToMenuButton.setCallbackData(BotState.GAME_MAIN.name());
        keyboardRows.add(List.of(backToMenuButton));

        markup.setKeyboard(keyboardRows);
        return markup;
    }


    @Override
    public BotState getState() {
        return BotState.GAME_PLATFORM_FILTER;
    }
}

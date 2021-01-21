package org.pyatsotok.bot.menu;

import org.pyatsotok.bot.domain.UserData;
import org.pyatsotok.randomizer.domain.GamePlatform;
import org.pyatsotok.randomizer.domain.GameStore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GameStoreMenuHandler implements MessageHandler {
    @Override
    public BotApiMethod<?> handle(UserData userData, String messageData) {
        userData.setState(getState());

        if (messageData != null && !messageData.isBlank()) {
            fillUserFilter(userData, messageData);
        }

        return updateMenu(userData);
    }

    private void fillUserFilter(UserData data, String storeParam) {
        Set<GameStore> stores = data.getGameFilter().getStores();
        GameStore store = GameStore.valueOf(storeParam);

        if (!stores.add(store)) {
            stores.remove(store);
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
        Set<GameStore> stores = data.getGameFilter().getStores();

        for (GameStore storeOption: GameStore.values()) {
            InlineKeyboardButton genreFilterButton = new InlineKeyboardButton();
            genreFilterButton.setText(storeOption.getTitle());
            genreFilterButton.setCallbackData(getState().name() + ":" + storeOption.name());

            if (stores.contains(storeOption)) {
                InlineKeyboardButton selectedMark = new InlineKeyboardButton();
                selectedMark.setText("<-");
                selectedMark.setCallbackData(getState().name() + ":" + storeOption.name());

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
        return BotState.GAME_STORE_FILTER;
    }
}

package org.randomizer.model;

import lombok.Data;
import org.randomizer.bot.menu.BotState;

@Data
public class UserData {

    private final Long id;
    private BotState state;
    private GameFilter gameFilter;
    private MovieFilter movieFilter;

    public UserData(Long id, BotState state) {
        this.id = id;
    }
}

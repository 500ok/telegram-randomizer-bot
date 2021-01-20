package org.pyatsotok.bot.domain;

import lombok.Data;
import org.pyatsotok.bot.menu.BotState;
import org.pyatsotok.randomizer.domain.GameFilter;
import org.pyatsotok.randomizer.domain.MovieFilter;

@Data
public class UserData {

    private final Long id;
    private Integer menuId;
    private BotState state;
    private GameFilter gameFilter = new GameFilter();
    private MovieFilter movieFilter = new MovieFilter();

    public UserData(Long id, BotState state) {
        this.id = id;
        this.state = state;
    }
}

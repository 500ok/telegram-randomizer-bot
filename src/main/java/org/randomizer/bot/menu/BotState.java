package org.randomizer.bot.menu;

public enum BotState {

    NEW("Main menu"),

    MAIN("Main menu"),

    GAME_MAIN("Game menu"),
    GAME_GENRE_FILTER("Genre filter"),
    GAME_PLATFORM_FILTER("Platform filter"),
    GAME_STORE_FILTER("Store filter"),

    MOVIE_MAIN("Movie menu"),
    MOVIE_GENRE_FILTER("Genre filter");

    private final String description;

    BotState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

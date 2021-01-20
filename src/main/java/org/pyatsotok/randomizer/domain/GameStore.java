package org.pyatsotok.randomizer.domain;

public enum GameStore {

    STEAM("steam"),
    PLAYSTATION_STORE("playstation-store"),
    XBOX_STORE("xbox-store"),
    APP_STORE("apple-appstore"),
    GOG("gog"),
    NINTENDO_STORE("nintendo"),
    GOOGLE_PLAY("google-play"),
    EPIC_GAMES("epic")

    ;

    private final String title;

    GameStore(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

package org.pyatsotok.randomizer.domain;

public enum GameStore {

    STEAM("Steam"),
    PLAYSTATION_STORE("Playstation store"),
    XBOX_STORE("Xbox Store"),
    APP_STORE("Appstore"),
    GOG("gog"),
    NINTENDO_STORE("Nintendo"),
    GOOGLE_PLAY("Google Play"),
    EPIC_GAMES("EGS");

    private final String title;

    GameStore(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

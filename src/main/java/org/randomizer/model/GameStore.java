package org.randomizer.model;

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

    private final String name;

    GameStore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getReadableName() {
        return this.toString().replaceAll("[\\-_]", " ");
    }

    public static String getValueByName(String name) {
        return GameStore.valueOf(name.toUpperCase().replaceAll("\\s", "_")).getName();
    }
}

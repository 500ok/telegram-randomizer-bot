package org.randomizer.model;

public enum GamePlatform {

    PC("pc"),
    PLAYSTATION5("playstation5"),
    XBOX_ONE("xbox-one"),
    PLAYSTATION4("playstation4"),
    XBOX_SERIES_X("xbox-series-x"),
    NINTENDO_SWITCH("nintendo-switch"),
    IOS("ios"),
    ANDROID("android"),
    MACOS("macOS"),
    LINUX("linux"),
    XBOX360("xbox360"),
    PLAYSTATION3("playstation3"),
    PLAYSTATION2("playstation2")

    ;

    private final String title;

    GamePlatform(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

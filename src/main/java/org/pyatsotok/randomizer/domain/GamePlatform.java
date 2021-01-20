package org.pyatsotok.randomizer.domain;

public enum GamePlatform {

    PC("PC"),
    PLAYSTATION5("Playstation 5"),
    XBOX_ONE("Xbox One"),
    PLAYSTATION4("Playstation 4"),
    XBOX_SERIES_X("Xbox Series X"),
    NINTENDO_SWITCH("Nintendo Switch"),
    IOS("IOS"),
    ANDROID("Android"),
    MACOS("MacOS"),
    LINUX("Linux"),
    XBOX360("Xbox360"),
    PLAYSTATION3("Playstation 3"),
    PLAYSTATION2("Playstation 2");

    private final String title;

    GamePlatform(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

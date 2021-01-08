package org.randomizer.model;

public enum GameGenre {
    ACTION("action"),
    INDIE("indie"),
    ADVENTURE("adventure"),
    RPG("rpg"),
    STRATEGY("strategy"),
    SHOOTER("shooter"),
    CASUAL("casual"),
    SIMULATION("simulation"),
    PUZZLE("puzzle"),
    ARCADE("arcade"),
    PLATFORMER("platformer"),
    RACING("racing"),
    SPORTS("sports"),
    MMO("massively-multiplayer"),
    FIGHTING("fighting"),
    FAMILY("family"),
    BOARD_GAMES("board-games"),
    EDUCATIONAL("educational"),
    CARD("card")

    ;

    private String name;

    GameGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getReadableName() {
        return this.toString().replaceAll("[\\-_]", " ");
    }

    public static GamePlatform getValueByName(String name) {
        return GamePlatform.valueOf(name.toUpperCase().replaceAll("\\s", "_"));
    }
}

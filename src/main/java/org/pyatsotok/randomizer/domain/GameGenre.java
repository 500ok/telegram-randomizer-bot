package org.pyatsotok.randomizer.domain;

public enum GameGenre {

    ACTION("Action"),
    INDIE("Indie"),
    ADVENTURE("Adventure"),
    RPG("RPG"),
    STRATEGY( "Strategy"),
    SHOOTER("Shooter"),
    CASUAL("Casual"),
    SIMULATION("Simulation"),
    PUZZLE("Puzzle"),
    ARCADE("Arcade"),
    PLATFORMER("Platformer"),
    RACING("Racing"),
    SPORTS("Sports"),
    MMO("MMO"),
    FIGHTING("Fighting"),
    FAMILY("Family"),
    BOARD_GAMES("Board games"),
    EDUCATIONAL("Educational"),
    CARD("Card");

    private final String title;

    GameGenre(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}

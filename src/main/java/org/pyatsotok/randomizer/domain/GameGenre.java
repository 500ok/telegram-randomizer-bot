package org.pyatsotok.randomizer.domain;

public enum GameGenre {

    ACTION(4, "Action"),
    INDIE(51, "Indie"),
    ADVENTURE(3, "Adventure"),
    RPG(5, "RPG"),
    STRATEGY(10, "Strategy"),
    SHOOTER(2, "Shooter"),
    CASUAL(40, "Casual"),
    SIMULATION(14, "Simulation"),
    PUZZLE(7, "Puzzle"),
    ARCADE(11, "Arcade"),
    PLATFORMER(83, "Platformer"),
    RACING(1, "Racing"),
    SPORTS(15, "Sports"),
    MMO(59, "MMO"),
    FIGHTING(6, "Fighting"),
    FAMILY(19, "Family"),
    BOARD_GAMES(28, "Board games"),
    EDUCATIONAL(34, "Educational"),
    CARD(17, "Card");

    private final int id;

    private final String title;

    GameGenre(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

}

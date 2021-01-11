package org.randomizer.model;

public enum GameGenre {
    ACTION(4),
    INDIE(51),
    ADVENTURE(3),
    RPG(5),
    STRATEGY(10),
    SHOOTER(2),
    CASUAL(40),
    SIMULATION(14),
    PUZZLE(7),
    ARCADE(11),
    PLATFORMER(83),
    RACING(1),
    SPORTS(15),
    MMO(59),
    FIGHTING(6),
    FAMILY(19),
    BOARD_GAMES(28),
    EDUCATIONAL(34),
    CARD(17)

    ;

    private int id;

    GameGenre(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GamePlatform getValueByName(String name) {
        return GamePlatform.valueOf(name);
    }
}

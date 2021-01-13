package org.randomizer.model;

import lombok.Data;

import java.util.Set;

@Data
public class GameFilter {

    private Set<GamePlatform> platforms;
    private Set<GameGenre> genres;
    private Set<GameStore> stores;

    public void clearFilter() {
        platforms.clear();
        genres.clear();
        stores.clear();
    }

}

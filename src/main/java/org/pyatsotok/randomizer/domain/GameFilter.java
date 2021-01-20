package org.pyatsotok.randomizer.domain;

import lombok.Data;

import java.util.Set;

@Data
public class GameFilter {

    private Set<GamePlatform> platforms;
    private Set<GameGenre> genres;
    private Set<GameStore> stores;

    public void clear() {
        platforms.clear();
        genres.clear();
        stores.clear();
    }

}

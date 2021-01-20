package org.pyatsotok.randomizer.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
public class GameFilter {

    private Set<GamePlatform> platforms = new HashSet<>();
    private Set<GameGenre> genres = new HashSet<>();
    private Set<GameStore> stores = new HashSet<>();

    public void clear() {
        platforms.clear();
        genres.clear();
        stores.clear();
    }

    public boolean isEmpty() {
        return platforms.isEmpty()
                && genres.isEmpty()
                && stores.isEmpty();
    }

}

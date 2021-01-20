package org.pyatsotok.randomizer.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
public class MovieFilter {

    private Set<MovieGenre> genres = new HashSet<>();

    public void clear() {
        genres.clear();
    }

    public boolean isEmpty() {
        return genres.isEmpty();
    }
}

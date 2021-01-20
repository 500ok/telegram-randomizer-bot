package org.pyatsotok.randomizer.domain;

import lombok.Data;

import java.util.Set;

@Data
public class MovieFilter {

    private Set<MovieGenre> genres;

    public void clear() {
        genres.clear();
    }
}

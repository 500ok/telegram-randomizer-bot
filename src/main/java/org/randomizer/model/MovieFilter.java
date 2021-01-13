package org.randomizer.model;

import lombok.Data;

import java.util.Set;

@Data
public class MovieFilter {

    private Set<MovieGenre> genres;

    public void clearFilter() {
        genres.clear();
    }
}

package org.pyatsotok.randomizer.source;

import org.pyatsotok.randomizer.domain.Movie;
import org.pyatsotok.randomizer.domain.MovieFilter;

public interface MovieSource {
    Movie getRandomMovie();
    Movie getRandomMovie(MovieFilter filter);
}

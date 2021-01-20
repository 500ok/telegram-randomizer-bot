package org.pyatsotok.randomizer.randomizer;

import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.Movie;
import org.pyatsotok.randomizer.domain.MovieFilter;
import org.pyatsotok.randomizer.source.MovieSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MovieRandomizer {

    private final MovieSource source;

    public MovieRandomizer(MovieSource source) {
        this.source = source;
    }

    public Movie getRandomMovie() {
        return source.getRandomMovie();
    }

    public Movie getRandomMovie(MovieFilter filter) {
        return source.getRandomMovie(filter);
    }

}

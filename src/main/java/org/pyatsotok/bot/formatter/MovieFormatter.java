package org.pyatsotok.bot.formatter;

import org.pyatsotok.randomizer.domain.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieFormatter implements MessageFormatter<Movie> {
    private static final String posterPath = "https://image.tmdb.org/t/p/original";

    @Override
    public String getFormattedMessage(Movie movie) {

        return "*Title*: \n" + movie.getTitle() + '\n' + "*Overview*: \n" +
                movie.getOverview().replaceAll("[_*<>]", "") +
                '\n' +
                "*Genres*: \n" + movie.getGenres() + '\n' +
                "*Spoken languages*: \n" + movie.getSpokenLanguages() + '\n' +
                "*Release date*: \n" + movie.getReleaseDate() + '\n' +
                "*Status*: \n" + movie.getStatus() + "\n" +
                String.format("[poster](%s) ", posterPath + movie.getPoster());
    }
}

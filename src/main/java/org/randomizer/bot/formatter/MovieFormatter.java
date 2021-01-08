package org.randomizer.bot.formatter;

import org.randomizer.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieFormatter implements MessageFormatter<Movie> {
    private static final String posterPath = "https://image.tmdb.org/t/p/original";

    @Override
    public String getFormattedMessage(Movie movie) {
        StringBuilder builder = new StringBuilder("*Title*: \n" + movie.getTitle() + '\n');
        builder.append("*Overview*: \n")
                .append(movie.getOverview().replaceAll("[_*<>]", ""))
                .append('\n');
        builder.append("*Genres*: \n").append(movie.getGenres()).append('\n');
        builder.append("*Spoken languages*: \n").append(movie.getSpokenLanguages()).append('\n');
        builder.append("*Release date*: \n").append(movie.getReleaseDate()).append('\n');
        builder.append("*Status*: \n").append(movie.getStatus()).append("\n");
        builder.append(String.format("[poster](%s) ", posterPath + movie.getPoster()));

        return builder.toString();
    }
}

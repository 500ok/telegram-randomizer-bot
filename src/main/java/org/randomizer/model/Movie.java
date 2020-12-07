package org.randomizer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.randomizer.util.MovieDeserializer;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonDeserialize(using = MovieDeserializer.class)
public class Movie implements FormattedMessage {

    private static final String posterPath = "https://image.tmdb.org/t/p/original";
    private String title;
    private LocalDate releaseDate;
    private String poster;
    private String status;
    private String overview;
    private List<String> spokenLanguages;
    private List<String> genres;

    @Override
    public String getFormattedMessage() {
        StringBuilder builder = new StringBuilder("*Title*: \n" + title + '\n');
        builder.append("*Overview*: \n")
                .append(overview.replaceAll("[_*<>]", ""))
                .append('\n');
        builder.append("*Genres*: \n").append(genres).append('\n');
        builder.append("*Spoken languages*: \n").append(spokenLanguages).append('\n');
        builder.append("*Release date*: \n").append(releaseDate).append('\n');
        builder.append("*Status*: \n").append(status).append("\n");
        builder.append(String.format("[poster](%s) ", posterPath + poster));

        return builder.toString();
    }
}

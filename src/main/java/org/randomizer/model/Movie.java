package org.randomizer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.randomizer.util.MovieDeserializer;

import java.time.LocalDate;
import java.util.List;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPoster(String poster) {
        this.poster = posterPath + poster;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setSpokenLanguages(List<String> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

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
        builder.append(String.format("[poster](%s) ", poster));

        return builder.toString();
    }
}

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
public class Movie {

    private String title;
    private LocalDate releaseDate;
    private String poster;
    private String status;
    private String overview;
    private List<String> spokenLanguages;
    private List<String> genres;

}

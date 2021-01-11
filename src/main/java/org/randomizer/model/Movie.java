package org.randomizer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Movie {

    private String title;
    private LocalDate releaseDate;
    private String poster;
    private String status;
    private String overview;
    private List<String> spokenLanguages;
    private List<String> genres;

}

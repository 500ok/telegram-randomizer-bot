package org.pyatsotok.randomizer.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Movie {

    private String title;
    private LocalDate releaseDate;
    private String poster;
    private String status;
    private String overview;
    private List<String> spokenLanguages;
    private List<String> genres;

}

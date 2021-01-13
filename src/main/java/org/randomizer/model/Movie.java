package org.randomizer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
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

package org.randomizer.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.randomizer.util.GameDeserializer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@JsonDeserialize(using = GameDeserializer.class)
public class Game {

    private String name;
    private String backgroundImage;
    private String description;
    private List<String> platforms;
    private List<String> genres;
    private LocalDate releaseDate;
    private Map<String, String> stores;

}

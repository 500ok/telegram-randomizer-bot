package org.pyatsotok.randomizer.domain;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class Game {

    private String name;
    private String backgroundImage;
    private String description;
    private List<String> platforms;
    private List<String> genres;
    private LocalDate releaseDate;
    private Map<String, String> stores;

    public void addPlatform(String platform) {
        this.platforms.add(platform);
    }

    public void addGenre(String genre) {
        this.genres.add(genre);
    }

    public void addStore(String name, String link) {
        this.stores.put(name, link);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", platforms=" + platforms +
                ", genres=" + genres +
                ", releaseDate=" + releaseDate +
                ", stores=" + stores +
                '}';
    }
}

package org.randomizer.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.randomizer.util.GameDeserializer;

import java.util.List;
import java.util.Map;

@JsonDeserialize(using = GameDeserializer.class)
public class Game {

    private String name;
    private String backgroundImage;
    private String description;
    private List<String> platforms;
    private List<String> genres;
    private Map<String, String> stores;

    public String getName() {
        return name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Map<String, String> getStores() {
        return stores;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setStores(Map<String, String> stores) {
        this.stores = stores;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(
                "*Name*: \n" + name + '\n' +
                "*Description*: \n" + description + '\n' +
                "*Genres*: \n" + genres + '\n' +
                "*Platforms*: \n" + platforms + '\n');

        builder.append("*Stores*: \n");

        for (Map.Entry<String, String> store: stores.entrySet()) {
            builder.append(String.format("[%s](%s)",
                    store.getKey(), store.getValue()));
        }

        builder.append("[ ](").append(backgroundImage).append(")");

        return builder.toString();
    }
}

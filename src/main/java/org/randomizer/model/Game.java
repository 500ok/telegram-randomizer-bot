package org.randomizer.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.randomizer.util.GameDeserializer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@JsonDeserialize(using = GameDeserializer.class)
public class Game implements FormattedMessage {

    private String name;
    private String backgroundImage;
    private String description;
    private List<String> platforms;
    private List<String> genres;
    private LocalDate releaseDate;
    private boolean redirect;
    private Map<String, String> stores;

    public Game() {}

    public String getName() {
        return name;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
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

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setName(String name) {
        if (name.length() > 2500) {
            name = name.substring(2500) + "...";
        }
        this.name = name;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setDescription(String description) { this.description = description; }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setStores(Map<String, String> stores) {
        this.stores = stores;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String format() {
        StringBuilder builder = new StringBuilder("*Name*: \n" + name + '\n');
        if (description != null) {
            builder.append("*Description*: \n")
                    .append(description.replaceAll("[_*<>]", ""))
                    .append('\n');
        }
        builder.append("*Genres*: \n").append(genres).append('\n');
        builder.append("*Release date*: \n").append(releaseDate).append('\n');
        builder.append("*Platforms*: \n").append(platforms).append('\n');
        builder.append("*Stores*: \n");

        for (Map.Entry<String, String> store: stores.entrySet()) {
            builder.append(String.format("_%s_ `%s`\n",
                    store.getKey(), store.getValue()));
        }

        builder.append(String.format("[image](%s) ", backgroundImage));

        return builder.toString();
    }
}

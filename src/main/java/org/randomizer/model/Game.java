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
public class Game implements FormattedMessage {

    private String name;
    private String backgroundImage;
    private String description;
    private List<String> platforms;
    private List<String> genres;
    private LocalDate releaseDate;
    private boolean redirect;
    private Map<String, String> stores;


    @Override
    public String getFormattedMessage() {
        StringBuilder builder = new StringBuilder("*Name*: \n" + name + '\n');
        if (description != null && !description.equals("")) {
            if (description.length() > 3500) description = description.substring(3500).trim() + "...";
            builder.append("*Description*: \n")
                    .append(description.replaceAll("[_*<>]", ""))
                    .append('\n');
        }

        if (genres != null) {
            builder.append("*Genres*: \n").append(genres).append('\n');
        }

        if (releaseDate != null) {
            builder.append("*Release date*: \n").append(releaseDate).append('\n');
        }

        builder.append("*Platforms*: \n").append(platforms).append('\n');

        if (stores != null && !stores.isEmpty()) {
            builder.append("*Stores*: \n");
            builder.append(String.format("%s\n", stores.keySet()));
        }

        if (backgroundImage != null) {
            builder.append(String.format("[-](%s) ", backgroundImage));
        }

        return builder.toString();
    }
}

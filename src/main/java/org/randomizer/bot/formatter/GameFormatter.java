package org.randomizer.bot.formatter;

import org.randomizer.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameFormatter implements MessageFormatter<Game> {
    @Override
    public String getFormattedMessage(Game game) {
        if (game == null) return null;
        StringBuilder builder = new StringBuilder("*Name*: \n" + game.getName() + '\n');
        if (game.getDescription() != null && !game.getDescription().equals("")) {
            if (game.getDescription().length() > 3500)
                game.setDescription(game.getDescription().substring(3500).trim() + "...");
            builder.append("*Description*: \n")
                    .append(game.getDescription().replaceAll("[_*<>]", ""))
                    .append('\n');
        }

        if (game.getGenres() != null) {
            builder.append("*Genres*: \n").append(game.getGenres()).append('\n');
        }

        if (game.getReleaseDate() != null) {
            builder.append("*Release date*: \n").append(game.getReleaseDate()).append('\n');
        }

        builder.append("*Platforms*: \n").append(game.getPlatforms()).append('\n');

        if (game.getStores() != null && !game.getStores().isEmpty()) {
            builder.append("*Stores*: \n");
            builder.append(String.format("%s\n", game.getStores().keySet()));
        }

        if (game.getBackgroundImage() != null) {
            builder.append(String.format("[-](%s) ", game.getBackgroundImage()));
        }

        return builder.toString();
    }
}

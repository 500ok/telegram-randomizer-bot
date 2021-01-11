package org.randomizer.util;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class RAWGGameDeserializer implements GameDeserializer {

    public Game deserialize(JsonNode root) {
        log.debug("Deserializing game");

        JSONObject rootObj = root.getObject();
        Game game = new Game();

        if (rootObj.has("redirect")) {
            game.setName("redirect");
            game.setDescription(rootObj.getString("slug"));
            return game;
        }

        game.setName(rootObj.getString("name"));
        game.setDescription(rootObj.getString("description_raw"));
        game.setBackgroundImage(rootObj.getString("background_image"));

        String released =  !rootObj.isNull("released")? rootObj.getString("released"): null;
        LocalDate releasedDate = released != null? LocalDate.parse(released): null;
        game.setReleaseDate(releasedDate);

        JSONArray platforms = rootObj.getJSONArray("platforms");

        if (platforms.length() > 0)
            game.setPlatforms(new LinkedList<>());

        for (int i = 0; i < platforms.length(); i++) {
            game.addPlatform(platforms.getJSONObject(i).getJSONObject("platform").getString("name"));
        }

        JSONArray genresNode = rootObj.getJSONArray("genres");
        if (genresNode.length() > 0)
            game.setGenres(new LinkedList<>());

        for (int i = 0; i < genresNode.length(); i++) {
            game.addGenre(genresNode.getJSONObject(i).getString("name"));
        }

        JSONArray stores = rootObj.getJSONArray("stores");
        if (stores.length() > 0)
            game.setStores(new HashMap<>());

        for (int i = 0; i < stores.length(); i++) {
            game.addStore(
                    stores.getJSONObject(i).getJSONObject("store").getString("name"),
                    stores.getJSONObject(i).getString("url")
            );
        }

        log.debug("Game \"{}\" deserialized", game.getName());
        return game;
    }

    public int deserializeId(JsonNode node) {
        return ThreadLocalRandom.current()
                .nextInt(0 , node.getObject().getInt("count"));
    }
}

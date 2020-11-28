package org.randomizer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.randomizer.model.Game;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameDeserializer extends StdDeserializer<Game> {

    public GameDeserializer() {
        this(null);
    }

    protected GameDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException
    {
        Game game = new Game();
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        game.setName(root.get("name").textValue());
        game.setDescription(root.get("description_raw").textValue());
        game.setBackgroundImage(root.get("background_image").textValue());


        JsonNode platformsNode = root.get("platforms");
        if (platformsNode.isArray()){
            List<String> platforms = new LinkedList<>();
            for (JsonNode platform: platformsNode) {
                platforms.add(platform.get("platform").get("name").textValue());
            }
            game.setPlatforms(platforms);
        }

        JsonNode genresNode = root.get("genres");
        if (genresNode.isArray()) {
            List<String> genres = new LinkedList<>();
            for (JsonNode genre: genresNode) {
                genres.add(genre.get("name").textValue());
            }
            game.setGenres(genres);
        }

        JsonNode storesNode = root.get("stores");
        if (storesNode.isArray()) {
            Map<String, String> stores = new HashMap<>();
            for (JsonNode store: storesNode) {
                stores.put(
                        store.get("store").get("name").textValue(),
                        store.get("url").textValue()
                );
            }
            game.setStores(stores);
        }

        return game;
    }
}

package org.pyatsotok.randomizer.source;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class RAWGGameSource implements GameSource {

    private static final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    @Value("${game.token}")
    private String token;
    private Map<String, String> headers;

    @PostConstruct
    private void init() {
        headers = Map.of(
                "x-rapidapi-key", token,
                "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
        );
    }

    @Override
    public Game getRandomGame() {
        String randomId = generateGameId();
        Game game = getGameById(randomId);
        log.debug("Game randomized: {}", randomId);
        return game;
    }

    private Game getGameById(String id) {

        HttpRequest<?> request = Unirest.get(servicePath + "/{id}")
                .routeParam("id", id)
                .headers(headers);
        log.debug("Request {} game: {}", id, request.getUrl());

        HttpResponse<JsonNode> response = request.asJson();

        log.debug("Game {} response status: {} {}", id, response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            log.error("Error while retrieving {} game", id);
            return null;
        }

        Game game = deserialize(response.getBody());

        if (game.getName().equals("redirect")) {
            log.debug("Received redirect game, request redirected game {}", game.getName());
            return getGameById(game.getName());
        }

        return game;
    }

    private String generateGameId() {
        HttpRequest<?> request = Unirest
                .get(servicePath)
                .queryString("stores", "1,2,3,5,6,10,11")
                .queryString("page_size", 1)
                .headers(headers);

        log.debug("Generating random game id: {}", request.getUrl());

        HttpResponse<JsonNode> response = request.asJson();

        log.debug("Game id response status {}", response.getStatus());
        if (response.getStatus() != 200) {
            log.error("Error getting game id");
            return null;
        }

        String identifier = String.valueOf(deserializeId(response.getBody()));

        log.debug("Generated game id {}", identifier);
        return identifier;
    }

    private Game deserialize(JsonNode root) {
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
        game.setBackgroundImage(!rootObj.isNull("background_image")?
                rootObj.getString("background_image"): null);

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

    private int deserializeId(JsonNode node) {
        return ThreadLocalRandom.current()
                .nextInt(0 , node.getObject().getInt("count"));
    }

}

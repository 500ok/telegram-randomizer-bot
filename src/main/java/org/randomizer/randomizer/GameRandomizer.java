package org.randomizer.randomizer;

import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.randomizer.config.Config;
import org.randomizer.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GameRandomizer {

    private static final Random random = new Random();
    private static final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    private static final String token = Config.getProperty("game.token");
    private static final Map<String, String> headers;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRandomizer.class);

    static  {
        headers = Map.of(
                "x-rapidapi-key", token,
                "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
        );
    }

    public Game getRandomGame() {
        String randomId =  generateGameId();
        Game game = getGameById(randomId);
        LOGGER.debug("Game randomized: {}", game.getName());
        return game;
    }

    private Game getGameById(String id) {

        HttpRequest<?> request = Unirest.get(servicePath + "/{id}")
                                    .routeParam("id", id)
                                    .headers(headers);
        LOGGER.debug("Request {} game: {}", id, request.getUrl());

        HttpResponse<Game> response = request.asObject(Game.class);

        LOGGER.debug("Game {} response status: {}", response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            LOGGER.error("Trying to get {} game again", id);
            return getGameById(id);
        }

        Game game = response.getBody();

        if (game.isRedirect()) {
            LOGGER.debug("Received redirect game, request redirected game {}", game.getName());
            return getGameById(game.getName());
        }

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            LOGGER.error("Unirest game {} parsing exception {}: {}",
                    id,
                    request.getUrl(),
                    exception.get().toString());
            LOGGER.error("Trying get game with id {}", id);
            return getGameById(id);
        }

        return game;
    }

    private String generateGameId() {
        HttpRequest<?> request = Unirest
                .get(servicePath)
                .queryString("stores", List.of(1,2,3,5,6,10,11))
                .queryString("page_size", 1)
                .headers(headers);

        LOGGER.debug("Generating random id: {}", request.getUrl());

        HttpResponse<JsonNode> response = request.asJson();

        LOGGER.debug("Game id response status {}", response.getStatus());
        if (response.getStatus() != 200) {
            LOGGER.error("Error getting game id, retrying to get id");
            return generateGameId();
        }

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            LOGGER.error("Unirest id parsing exception {}: {}",
                    exception.get().getOriginalBody(),
                    exception.get().toString()
            );
            LOGGER.error("Trying to get a random game id");
            return generateGameId();
        }

        JSONObject jsonBody = response.getBody().getObject();
        String identifier = String.valueOf(random.nextInt((int) jsonBody.get("count")));

        LOGGER.debug("Generated id {}", identifier);
        return identifier;
    }
}

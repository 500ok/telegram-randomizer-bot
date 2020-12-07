package org.randomizer.randomizer;

import kong.unirest.*;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class GameRandomizer {

    private static final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    private final String token;
    private final Map<String, String> headers;

    public GameRandomizer(@Value("${game.token}") String token) {
        this.token = token;
        headers = Map.of(
                    "x-rapidapi-key", token,
                    "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
        );
    }



    public Game getRandomGame() {
        String randomId =  generateGameId();
        Game game = getGameById(randomId);
        log.debug("Game randomized: {}", randomId);
        return game;
    }

    private Game getGameById(String id) {

        HttpRequest<?> request = Unirest.get(servicePath + "/{id}")
                                    .routeParam("id", id)
                                    .headers(headers);
        log.debug("Request {} game: {}", id, request.getUrl());

        HttpResponse<Game> response = request.asObject(Game.class);

        log.debug("Game {} response status: {} {}", id, response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            log.error("Error while retrieving {} game", id);
            return null;
        }

        Game game = response.getBody();

        if (game.isRedirect()) {
            log.debug("Received redirect game, request redirected game {}", game.getName());
            return getGameById(game.getName());
        }

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            log.error("Unirest game {} parsing exception {}: {}",
                    id,
                    request.getUrl(),
                    exception.get().toString());
            return null;
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

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            log.error("Unirest game id parsing exception {}: {}",
                    exception.get().getOriginalBody(),
                    exception.get().toString()
            );
            return null;
        }

        JSONObject jsonBody = response.getBody().getObject();
        String identifier = String.valueOf(ThreadLocalRandom.current()
                .nextInt(0 ,(int) jsonBody.get("count")));

        log.debug("Generated game id {}", identifier);
        return identifier;
    }
}

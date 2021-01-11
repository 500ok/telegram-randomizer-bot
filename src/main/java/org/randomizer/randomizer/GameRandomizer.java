package org.randomizer.randomizer;

import kong.unirest.*;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Game;
import org.randomizer.util.GameDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Service
public class GameRandomizer {

    private static final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    @Value("${game.token}")
    private String token;
    private Map<String, String> headers;
    private GameDeserializer deserializer;

    @Autowired
    public GameRandomizer(GameDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @PostConstruct
    private void init() {
        headers = Map.of(
                "x-rapidapi-key", token,
                "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
        );
    }



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

        Game game = deserializer.deserialize(response.getBody());

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

        String identifier = String.valueOf(deserializer.deserializeId(response.getBody()));

        log.debug("Generated game id {}", identifier);
        return identifier;
    }
}

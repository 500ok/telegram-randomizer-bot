package org.randomizer.randomizer;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestParsingException;
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
        int random =  generateGameNumber();
        LOGGER.debug("Request game with number {}", random);

        HttpResponse<Game> response = Unirest
                                        .get(servicePath + "/{id}")
                                        .routeParam("id",String.valueOf(random))
                                        .headers(headers)
                                        .asObject(Game.class);


        LOGGER.debug("Response status {}: {}", response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            LOGGER.debug("Trying to get game again");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
                return null;
            }
            return getRandomGame();
        }

        Game game = response.getBody();

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            LOGGER.error("Unirest parsing exception with response body {}: {}",
                    response.getBody(),
                    exception.get().getMessage());
            return null;
        }

        LOGGER.debug("Randomized game: {}", game.getName());

        return game;
    }

    private int generateGameNumber() {
        LOGGER.debug("Generating random number");
        HttpResponse<JsonNode> response = Unirest
                .get(servicePath)
                .queryString("stores", List.of(1,2,3,5,6,10,11))
                .queryString("page_size", 1)
                .headers(headers)
                .asJson();

        return random.nextInt((int) response.getBody().getObject().get("count"));
    }
}

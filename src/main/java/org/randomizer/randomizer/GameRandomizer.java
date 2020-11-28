package org.randomizer.randomizer;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.randomizer.config.Config;
import org.randomizer.model.Game;

import java.util.Map;
import java.util.Random;

public class GameRandomizer {

    private static final Random random = new Random();
    private static final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    private static final String token = Config.getProperty("game.token");
    private static final Map<String, String> headers;

    static  {
        headers = Map.of(
                "x-rapidapi-key", token,
                "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
        );
    }

    public Game getRandomGame() {
        int random =  generateGameNumber();

        return Unirest
                    .get(servicePath + "/{id}")
                    .routeParam("id",String.valueOf(random))
                    .headers(headers)
                    .asObject(Game.class)
                    .getBody();
    }

    private int generateGameNumber() {
        HttpResponse<JsonNode> response = Unirest
                .get(servicePath)
                .queryString("page_size", 1)
                .headers(headers)
                .asJson();

        return random.nextInt((int) response.getBody().getObject().get("count"));
    }
}

package org.randomizer.randomizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.randomizer.config.Config;
import org.randomizer.model.Game;

import java.util.Random;

public class GameRandomizer {

    private final Gson gson = new Gson();
    private final Random random = new Random();
    private final String servicePath = "https://rawg-video-games-database.p.rapidapi.com/games";
    private final String token = Config.getProperty("game.token");
    private final ObjectMapper mapper = new ObjectMapper();

    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Game getRandomGame() {
        int random =  getRandomGamePage();

        System.out.println(random);

        JsonNode json = Unirest
                            .get(servicePath + "/{id}")
                            .routeParam("id",String.valueOf(random))
                            .responseEncoding("UTF-8")
                            .header("x-rapidapi-key", token)
                            .header("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
                            .asJson().getBody();


        try {
            return mapper.readValue(json.toString(), Game.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRandomGamePage() {
        HttpResponse<JsonNode> response = Unirest
                .get(servicePath)
                .queryString("page_size", 1)
                .header("x-rapidapi-key", token)
                .header("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
                .asJson();

        return random.nextInt((int) response.getBody().getObject().get("count"));
    }
}

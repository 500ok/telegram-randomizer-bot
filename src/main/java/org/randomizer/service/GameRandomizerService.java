package org.randomizer.service;

import org.randomizer.client.ConnectionClient;
import org.randomizer.config.Config;

import java.net.URI;
import java.util.Map;

public class GameRandomizerService {

    private URI servicePath = URI.create(Config.getProperty("game.uri"));
    private String token = Config.getProperty("game.token");
    private ConnectionClient client = ConnectionClient.getInstance();

    public String getRandomGame() {
        return client.get(servicePath,
                Map.of(
                        "x-rapidapi-key", token,
                        "x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com"
                )
        );
    }
}

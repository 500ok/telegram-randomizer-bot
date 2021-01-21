package org.pyatsotok.randomizer.source;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static org.pyatsotok.randomizer.domain.GameGenre.*;
import static org.pyatsotok.randomizer.domain.GamePlatform.*;
import static org.pyatsotok.randomizer.domain.GameStore.*;

@Slf4j
@Component
public class RAWGGameSource implements GameSource {

    private static final String servicePath = "https://api.rawg.io/api/games";
    private static final Map<GameGenre, Integer> genreIds = Map.ofEntries(
            Map.entry(ACTION, 4), Map.entry(INDIE, 51),
            Map.entry(ADVENTURE, 3), Map.entry(RPG, 5),
            Map.entry(STRATEGY, 10), Map.entry(SHOOTER, 2),
            Map.entry(CASUAL, 40), Map.entry(SIMULATION, 14),
            Map.entry(PUZZLE, 7), Map.entry(ARCADE, 11),
            Map.entry(PLATFORMER, 83), Map.entry(RACING, 1),
            Map.entry(SPORTS, 15), Map.entry(MMO, 59),
            Map.entry(FIGHTING, 6), Map.entry(FAMILY, 19),
            Map.entry(BOARD_GAMES, 28), Map.entry(EDUCATIONAL, 34),
            Map.entry(CARD, 17)
    );
    private static final Map<GamePlatform, Integer> platformIds = Map.ofEntries(
            Map.entry(PC, 4), Map.entry(PLAYSTATION5, 187),
            Map.entry(XBOX_ONE, 4166), Map.entry(PLAYSTATION4, 18),
            Map.entry(XBOX_SERIES_X, 186), Map.entry(NINTENDO_SWITCH, 7),
            Map.entry(IOS, 3), Map.entry(ANDROID, 21),
            Map.entry(MACOS, 5), Map.entry(LINUX, 6),
            Map.entry(XBOX360, 14), Map.entry(PLAYSTATION3, 16),
            Map.entry(PLAYSTATION2, 15)
    );
    private static final Map<GameStore, Integer> storeIds = Map.ofEntries(
            Map.entry(STEAM, 1), Map.entry(PLAYSTATION_STORE, 3),
            Map.entry(XBOX_STORE, 2), Map.entry(APP_STORE, 4),
            Map.entry(GOG, 5), Map.entry(NINTENDO_STORE, 6),
            Map.entry(GOOGLE_PLAY, 8), Map.entry(EPIC_GAMES, 11)
    );

    @Value("${game.token}")
    private String token;

    @Override
    public Game getRandomGame() {
        String randomId = getRandomId();
        if (randomId == null) return null;
        Game game = getGameById(randomId);
        if (game == null) return null;
        log.debug("Game randomized: {}", game.toString());
        return game;
    }

    @Override
    public Game getRandomGame(GameFilter filter) {
        String randomId = getRandomId(filter);
        if (randomId == null) return  null;
        Game game = getGameById(randomId);
        if (game == null) return  null;
        log.debug("Game {} randomized with filter {}", game.toString(), filter.toString());
        return game;
    }

    private Game getGameById(String id) {
        HttpRequest<?> request = Unirest
                .get(servicePath + "/{id}")
                .routeParam("id", id)
                .queryString("key", token);
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

    private String getRandomId(GameFilter filter) {
        HttpRequest<?> request = Unirest
                .get(servicePath)
                .queryString("key", token)
                .queryString("page_size", 1);
        fillRequestWithFilter(request, filter);
        log.debug("Generating random game id with filter: {}; {}", filter.toString(), request.getUrl());

        HttpResponse<JsonNode> response = request.asJson();
        log.debug("Filter game id response status {}", response.getStatus());
        if (response.getStatus() != 200) {
            log.error("Error getting game id with filter");
            return null;
        }

        String identifier = String.valueOf(deserializeId(response.getBody()));
        log.debug("Generated game id {} with filter", identifier);
        return identifier;
    }

    private String getRandomId() {
        HttpRequest<?> request = Unirest
                .get(servicePath)
                .queryString("key", token);
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

    private void fillRequestWithFilter(HttpRequest<?> request, GameFilter filter) {
        List<String> strParams = new ArrayList<>();

        if (!filter.getPlatforms().isEmpty()) {
            filter.getPlatforms().forEach((e) -> strParams.add(String.valueOf(platformIds.get(e))));
            request.queryString("platforms", String.join(",", strParams));
            strParams.clear();
        }

        if (!filter.getStores().isEmpty()) {
            filter.getStores().forEach((e) -> strParams.add(String.valueOf(storeIds.get(e))));
            request.queryString("stores", String.join(",", strParams));
            strParams.clear();
        }

        if (!filter.getGenres().isEmpty()) {
            filter.getGenres().forEach((e) -> strParams.add(String.valueOf(genreIds.get(e))));
            request.queryString("genres", String.join(",", strParams));
            strParams.clear();
        }
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

    private Integer deserializeId(JsonNode node) {
        Integer count = node.getObject().getInt("count");
        if (count < 1) return null;
        return ThreadLocalRandom.current()
                .nextInt(0, count);
    }

}

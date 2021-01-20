package org.pyatsotok.randomizer.source;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class TMDBMovieSource implements MovieSource {
    private static final String servicePath = "https://api.themoviedb.org/3";
    @Value("${movie.token}")
    private String token;

    @Override
    public Movie getRandomMovie() {
        String randomId = getRandomMovieId();
        Movie movie = getRandomMovieById(randomId);
        log.debug("Movie randomized: {}", randomId);
        return movie;
    }

    private Movie getRandomMovieById(String id) {
        HttpRequest<?> request = Unirest.get(servicePath + "/movie/{id}")
                .queryString("api_key", token)
                .routeParam("id", id);

        log.debug("Request {} movie: {}", id, request.getUrl());
        HttpResponse<JsonNode> response = request.asJson();
        log.debug("Movie {} response status {}: {}", id, response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            log.error("Error while retrieving {} movie", id);
            return null;
        }

        return deserialize(response.getBody());
    }

    private String getRandomMovieId() {
        HttpRequest<?> request = Unirest.get(servicePath + "/discover/movie?sort_by=popularity.desc")
                .queryString("api_key", token);

        log.debug("Generating random movie id");
        HttpResponse<JsonNode> response = request.asJson();
        log.debug("Movie id response status {}", response.getStatus());

        if (response.getStatus() != 200) {
            log.error("Error getting movie id");
            return null;
        }

        String identifier = String.valueOf(deserializeId(response.getBody()));
        log.debug("Generated movie id {}", identifier);

        return identifier;
    }

    private Movie deserialize(JsonNode json) {
        log.debug("Deserializing movie");
        Movie movie = new Movie();
        JSONObject root = json.getObject();

        movie.setTitle(root.getString("title"));
        movie.setOverview(root.getString("overview"));
        movie.setPoster(root.getString("poster_path"));
        movie.setStatus(root.getString("status"));
        movie.setReleaseDate(LocalDate.parse(root.getString("release_date")));

        JSONArray languagesNode = root.getJSONArray("spoken_languages");
        List<String> languages = new LinkedList<>();
        for (int i = 0; i < languagesNode.length(); i++) {
            languages.add(languagesNode.getJSONObject(i).getString("name"));
        }
        movie.setSpokenLanguages(languages);

        JSONArray genresNode = root.getJSONArray("genres");
        List<String> genres = new LinkedList<>();
        for (int i = 0; i < genresNode.length(); i++) {
            genres.add(genresNode.getJSONObject(i).getString("name"));
        }
        movie.setGenres(genres);

        log.debug("Movie \"{}\" deserialized", movie.getTitle());
        return movie;
    }

    private int deserializeId(JsonNode json) {
        JSONObject root = json.getObject();
        JSONArray jsonMovies = root.getJSONArray("results");
        JSONObject jsonMovie = jsonMovies.getJSONObject(ThreadLocalRandom.current()
                .nextInt(jsonMovies.length()));

        return jsonMovie.getInt("id");
    }
}

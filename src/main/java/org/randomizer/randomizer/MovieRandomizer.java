package org.randomizer.randomizer;

import kong.unirest.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class MovieRandomizer {
    private static final String servicePath = "https://api.themoviedb.org/3";
    private final String token;
    private static final int total_pages = 500;

    public MovieRandomizer(@Value("${movie.token}") String token) {
        this.token = token;
    }

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
        HttpResponse<Movie> response = request.asObject(Movie.class);
        log.debug("Movie {} response status {}: {}", id, response.getStatus(), response.getStatusText());

        if (response.getStatus() != 200) {
            log.error("Error while retrieving {} movie", id);
            return null;
        }

        Movie movie = response.getBody();

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            log.error("Unirest movie {} parsing exception {}: {}",
                    id,
                    request.getUrl(),
                    exception.get().toString());
            return null;
        }

        return movie;
    }

    private String getRandomMovieId() {
        HttpRequest<?> request = Unirest.get(servicePath + "/discover/movie")
                .queryString("api_key", token)
                .queryString("page", ThreadLocalRandom.current().nextInt(total_pages));

        log.debug("Generating random movie id: {}", request.getUrl());
        HttpResponse<JsonNode> response = request.asJson();
        log.debug("Movie id response status {}", response.getStatus());

        if (response.getStatus() != 200) {
            log.error("Error getting movie id");
            return null;
        }

        Optional<UnirestParsingException> exception = response.getParsingError();
        if(exception.isPresent()){
            log.error("Unirest movie id parsing exception {}: {}",
                    exception.get().getOriginalBody(),
                    exception.get().toString()
            );
            return null;
        }

        JSONArray jsonMovies = response.getBody().getObject().getJSONArray("results");
        JSONObject jsonMovie = jsonMovies.getJSONObject(ThreadLocalRandom.current()
                .nextInt(jsonMovies.length()));

        String identifier = jsonMovie.get("id").toString();

        log.debug("Generated movie id {}", identifier);
        return identifier;
    }
}

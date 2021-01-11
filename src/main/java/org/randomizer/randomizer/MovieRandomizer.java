package org.randomizer.randomizer;

import kong.unirest.*;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Movie;
import org.randomizer.util.MovieDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MovieRandomizer {
    private static final String servicePath = "https://api.themoviedb.org/3";
    @Value("${movie.token}")
    private String token;
    private MovieDeserializer deserializer;

    @Autowired
    public MovieRandomizer(MovieDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    public MovieRandomizer() { }

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

        return deserializer.deserialize(response.getBody());
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

        String identifier = String.valueOf(deserializer.deserializeId(response.getBody()));
        log.debug("Generated movie id {}", identifier);

        return identifier;
    }
}

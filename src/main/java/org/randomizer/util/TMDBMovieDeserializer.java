package org.randomizer.util;

import kong.unirest.JsonNode;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Movie;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class TMDBMovieDeserializer implements MovieDeserializer {

    @Override
    public Movie deserialize(JsonNode json) {
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

    @Override
    public int deserializeId(JsonNode json) {
        JSONObject root = json.getObject();
        JSONArray jsonMovies = root.getJSONArray("results");
        JSONObject jsonMovie = jsonMovies.getJSONObject(ThreadLocalRandom.current()
                .nextInt(jsonMovies.length()));

        return jsonMovie.getInt("id");
    }
}

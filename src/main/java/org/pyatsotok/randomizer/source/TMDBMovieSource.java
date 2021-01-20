package org.pyatsotok.randomizer.source;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.Movie;
import org.pyatsotok.randomizer.domain.MovieFilter;
import org.pyatsotok.randomizer.domain.MovieGenre;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.pyatsotok.randomizer.domain.MovieGenre.*;

@Slf4j
@Component
public class TMDBMovieSource implements MovieSource {
    private static final String servicePath = "https://api.themoviedb.org/3";
    private static final Map<MovieGenre, Integer> genreIds = Map.ofEntries(
            Map.entry(ACTION, 28), Map.entry(ADVENTURE, 12),
            Map.entry(ANIMATION, 16), Map.entry(COMEDY, 35),
            Map.entry(CRIME, 80), Map.entry(DOCUMENTARY, 99),
            Map.entry(DRAMA, 18), Map.entry(FAMILY, 10751),
            Map.entry(FANTASY, 14), Map.entry(HISTORY, 36),
            Map.entry(HORROR, 27), Map.entry(MUSIC, 10402),
            Map.entry(MYSTERY, 9648), Map.entry(ROMANCE, 10749),
            Map.entry(SCIENCE_FICTION, 878), Map.entry(TV_MOVIE, 10770),
            Map.entry(THRILLER, 53), Map.entry(WAR, 10752),
            Map.entry(WESTERN, 37)
    );
    @Value("${movie.token}")
    private String token;

    @Override
    public Movie getRandomMovie() {
        String randomId = getRandomMovieId();
        if (randomId == null) return null;
        Movie movie = getRandomMovieById(randomId);
        log.debug("Movie randomized: {}", randomId);
        return movie;
    }

    @Override
    public Movie getRandomMovie(MovieFilter filter) {
        String randomId = getRandomMovieId(filter);
        if (randomId == null) return null;
        Movie movie = getRandomMovieById(randomId);
        log.debug("Filter Movie randomized: {}", randomId);
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

    private String getRandomMovieId(MovieFilter filter) {
        HttpRequest<?> request = Unirest.get(servicePath + "/discover/movie?sort_by=popularity.desc")
                .queryString("api_key", token);
        fillRequestWithFilter(request, filter);

        log.debug("Generating random movie id with filter: {}", filter.getGenres().toString());
        HttpResponse<JsonNode> response = request.asJson();
        log.debug("Filter Movie id response status {}", response.getStatus());

        if (response.getStatus() != 200) {
            log.error("Error getting filter movie id");
            return null;
        }

        String identifier = String.valueOf(deserializeId(response.getBody()));
        log.debug("Generated movie id {}", identifier);

        return identifier;
    }

    private void fillRequestWithFilter(HttpRequest<?> request, MovieFilter filter) {
        List<String> builder = new ArrayList<>();

        if (filter.getGenres().size() > 0)
        {
            for (MovieGenre genre: filter.getGenres()) {
                builder.add(String.valueOf(genreIds.get(genre)));
            }

            request.queryString("with_genres", String.join(",", builder));
        }
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

        Integer id = deserializeId(response.getBody());
        if (id == null) return null;

        String result = String.valueOf(id);
        log.debug("Generated movie id {}", id);

        return result;
    }

    private Movie deserialize(JsonNode json) {
        log.debug("Deserializing movie");
        Movie movie = new Movie();
        JSONObject root = json.getObject();

        movie.setTitle(root.getString("title"));
        movie.setOverview(root.getString("overview"));
        movie.setPoster(root.isNull("poster_path")? null: root.getString("poster_path"));
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

    private Integer deserializeId(JsonNode json) {
        JSONObject root = json.getObject();
        JSONArray jsonMovies = root.getJSONArray("results");
        int moviesCount = jsonMovies.length();
        if (moviesCount < 1) return null;
        JSONObject jsonMovie = jsonMovies.getJSONObject(ThreadLocalRandom.current()
                .nextInt(moviesCount));

        return jsonMovie.getInt("id");
    }
}

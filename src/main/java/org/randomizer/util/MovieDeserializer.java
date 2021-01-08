package org.randomizer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.randomizer.model.Movie;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class MovieDeserializer extends StdDeserializer<Movie> {

    public MovieDeserializer() {
        this(null);
    }
    protected MovieDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Movie deserialize(JsonParser jsonParser, DeserializationContext context) {
        log.debug("Deserializing movie");
        Movie movie = new Movie();
        JsonNode root;

        try {
            root = jsonParser.getCodec().readTree(jsonParser);
        } catch (IOException e) {
            log.error("Error while deserializing movie json: {}", e.toString());
            return null;
        }

        movie.setTitle(root.get("title").textValue());
        movie.setOverview(root.get("overview").textValue());
        movie.setPoster(root.get("poster_path").textValue());
        movie.setStatus(root.get("status").textValue());
        movie.setReleaseDate(LocalDate.parse(root.get("release_date").textValue()));

        JsonNode languagesNode = root.get("spoken_languages");
        if (languagesNode.isArray()){
            List<String> languages = new LinkedList<>();
            for (JsonNode language: languagesNode) {
                languages.add(language.get("name").textValue());
            }
            movie.setSpokenLanguages(languages);
        }

        JsonNode genresNode = root.get("genres");
        if (genresNode.isArray()) {
            List<String> genres = new LinkedList<>();
            for (JsonNode genre: genresNode) {
                genres.add(genre.get("name").textValue());
            }
            movie.setGenres(genres);
        }

        log.debug("Movie \"{}\" deserialized", movie.getTitle());
        return movie;
    }
}

package org.randomizer.util;

import kong.unirest.JsonNode;
import org.randomizer.model.Movie;

public interface MovieDeserializer {
    Movie deserialize(JsonNode json);
    int deserializeId(JsonNode json);
}

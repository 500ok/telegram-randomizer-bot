package org.randomizer.util;

import kong.unirest.JsonNode;
import org.randomizer.model.Game;

public interface GameDeserializer {
    Game deserialize(JsonNode node);
    int deserializeId(JsonNode node);
}

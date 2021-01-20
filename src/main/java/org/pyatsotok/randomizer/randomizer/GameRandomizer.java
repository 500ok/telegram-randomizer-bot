package org.pyatsotok.randomizer.randomizer;

import lombok.extern.slf4j.Slf4j;
import org.pyatsotok.randomizer.domain.Game;
import org.pyatsotok.randomizer.source.GameSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameRandomizer {

    private final GameSource source;

    @Autowired
    public GameRandomizer(GameSource source) {
        this.source = source;
    }

    public Game getRandomGame() {
        return source.getRandomGame();
    }
}

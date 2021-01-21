package org.pyatsotok.randomizer.source;

import org.pyatsotok.randomizer.domain.Game;
import org.pyatsotok.randomizer.domain.GameFilter;

public interface GameSource {
    Game getRandomGame();
    Game getRandomGame(GameFilter filter);
}

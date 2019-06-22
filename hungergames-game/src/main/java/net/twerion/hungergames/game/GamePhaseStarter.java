package net.twerion.hungergames.game;

import net.twerion.hungergames.game.Game;

public interface GamePhaseStarter {
  void start(Game game);

  boolean canStart(Game game);
}

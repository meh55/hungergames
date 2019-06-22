package net.twerion.hungergames.game;

public interface GamePhase {
  void begin();
  void end();

  GamePhaseConfig config();
  boolean isActive();
}

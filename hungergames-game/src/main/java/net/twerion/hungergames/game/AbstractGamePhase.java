package net.twerion.hungergames.game;

import net.twerion.hungergames.ListenerGroup;
import org.bukkit.event.Listener;

public abstract class AbstractGamePhase implements GamePhase, Listener {
  private GamePhaseConfig config;
  private ListenerGroup listeners;

  protected AbstractGamePhase(GamePhaseConfig config) {
    this.config = config;
  }

  @Override
  public GamePhaseConfig config() {
    return config;
  }

  @Override
  public void begin() {

  }

  @Override
  public void end() {

  }



  public void register() {}

  public void unregister() {}

  @Override
  public boolean isActive() {
    return false;
  }
}

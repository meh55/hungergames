package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GamePhaseEndEvent extends Event  {
  private static final HandlerList handlerList = new HandlerList();

  private GamePhase phase;

  private GamePhaseEndEvent(GamePhase phase) {
    this.phase = phase;
  }

  public GamePhase phase() {
    return phase;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public static GamePhaseEndEvent of(GamePhase phase) {
    Preconditions.checkNotNull(phase);
    return new GamePhaseEndEvent(phase);
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }
}

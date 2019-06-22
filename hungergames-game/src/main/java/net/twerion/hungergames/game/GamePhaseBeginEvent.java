package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GamePhaseBeginEvent extends Event {
  private static final HandlerList handlerList = new HandlerList();

  private GamePhase phase;

  private GamePhaseBeginEvent(GamePhase phase) {
    this.phase = phase;
  }

  public GamePhase phase() {
    return phase;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public static GamePhaseBeginEvent of(GamePhase phase) {
    Preconditions.checkNotNull(phase);
    return new GamePhaseBeginEvent(phase);
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }
}

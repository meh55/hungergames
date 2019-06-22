package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;
import net.twerion.hungergames.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Optional;

public final class TributeEliminateEvent extends Event {
  private static final HandlerList handlerList = new HandlerList();

  public enum Cause {
    DISCONNECT,
    KILLED,
    DIED
  }

  private User user;
  @Nullable private User killer;
  private Cause cause;

  private TributeEliminateEvent(User user, @Nullable User killer, Cause cause) {
    this.user = user;
    this.killer = killer;
    this.cause = cause;
  }

  public Cause cause() {
    return cause;
  }

  public Optional<User> killer() {
    return Optional.ofNullable(killer);
  }

  public User tribute() {
    return user;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public static TributeEliminateEvent of(User tribute, Cause cause) {
    Preconditions.checkNotNull(tribute);
    Preconditions.checkNotNull(cause);
    return new TributeEliminateEvent(tribute, null, cause);
  }

  public static TributeEliminateEvent of(
      User tribute, Cause cause, @Nullable User killer
  ) {
    Preconditions.checkNotNull(tribute);
    Preconditions.checkNotNull(cause);
    return new TributeEliminateEvent(tribute, null, cause);
  }
}

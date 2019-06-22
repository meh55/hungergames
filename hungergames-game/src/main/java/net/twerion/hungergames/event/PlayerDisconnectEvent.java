package net.twerion.hungergames.event;

import java.util.Optional;

import javax.annotation.Nullable;

import net.twerion.hungergames.Preconditions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerDisconnectEvent extends Event  {
  private static final HandlerList handlerList = new HandlerList();

  private Player player;

  @Nullable
  private String message;

  private PlayerDisconnectEvent(Player player, @Nullable String message) {
    this.player = player;
    this.message = message;
  }

  public Player player() {
    return player;
  }

  public Optional<String> message() {
    return Optional.ofNullable(message);
  }

  public void preventMessage() {
    this.message = null;
  }

  public void setMessage(String message) {
    Preconditions.checkNotNull(message);
    this.message = message;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public static PlayerDisconnectEvent of(Player player) {
    return of(player, null);
  }

  public static PlayerDisconnectEvent of(PlayerQuitEvent quit) {
    Preconditions.checkNotNull(quit);
    return new PlayerDisconnectEvent(quit.getPlayer(), quit.getQuitMessage());
  }

  public static PlayerDisconnectEvent of(PlayerKickEvent kick) {
    Preconditions.checkNotNull(kick);
    return new PlayerDisconnectEvent(kick.getPlayer(), kick.getLeaveMessage());
  }

  public static PlayerDisconnectEvent of(Player player, @Nullable String message) {
    Preconditions.checkNotNull(player);
    return new PlayerDisconnectEvent(player, message);
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }
}

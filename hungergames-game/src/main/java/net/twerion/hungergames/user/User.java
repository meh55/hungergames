package net.twerion.hungergames.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;

public final class User {
  private Team team;
  private Player player;
  private ReadWriteLock lock;
  private Records records;
  private volatile boolean alive;
  private volatile Player lastDamager;

  User(
    Team team,
    Player player,
    ReadWriteLock lock,
    Records records
  )  {
    this.team = team;
    this.player = player;
    this.lock = lock;
    this.records = records;
    this.alive = true;
  }

  public Player player() {
    return player;
  }

  public Team team() {
    return team;
  }

  public boolean isAlive() {
    return alive;
  }

  public void markAsSpectator() {
    this.alive = false;
  }

  public void applySpectatorVisibility(UserRepository users) {
    for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
      if (otherPlayer.equals(player)) {
        continue;
      }
      User other = users.getInstance(player);
      if (!other.isAlive()) {
        otherPlayer.hidePlayer(player);
      }
      player.hidePlayer(otherPlayer);
    }
  }

  public void hideGlobally() {
    Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(this.player));
  }

  public void showGlobally() {
    Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(this.player));
  }

  @Nullable
  public Player lastDamager() {
    return lastDamager;
  }

  public void setLastDamager(Player player) {
    this.lastDamager = player;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof User)) {
      return false;
    }
    return player.getUniqueId().equals(((User)other).player.getUniqueId());
  }

  @Override
  public int hashCode() {
    return player.getUniqueId().hashCode();
  }
}

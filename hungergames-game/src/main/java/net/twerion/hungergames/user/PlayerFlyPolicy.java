package net.twerion.hungergames.user;

import org.bukkit.entity.Player;

public enum PlayerFlyPolicy implements PlayerPolicy {
  ALLOW_FLYING {
    @Override
    public void apply(Player player) {
      player.setAllowFlight(true);
    }
  },
  DISALLOW_FLYING{
    @Override
    public void apply(Player player) {
      if (player.isFlying()) {
        player.setFlying(false);
      }
      player.setAllowFlight(false);
    }
  }
}

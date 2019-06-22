package net.twerion.hungergames.loot;

import org.bukkit.Location;

public interface TierProvider {
  float tierAtLocation(Location location);
}

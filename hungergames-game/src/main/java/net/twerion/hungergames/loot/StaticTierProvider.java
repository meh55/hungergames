package net.twerion.hungergames.loot;

import org.bukkit.Location;

public final class StaticTierProvider implements TierProvider {
  private float tier;

  private StaticTierProvider(float tier) {
    this.tier = tier;
  }

  @Override
  public float tierAtLocation(Location location) {
    return tier;
  }

  public static StaticTierProvider of(float tier) {
    return new StaticTierProvider(tier);
  }
}

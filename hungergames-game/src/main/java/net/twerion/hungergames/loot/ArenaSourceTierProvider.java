package net.twerion.hungergames.loot;

import com.google.inject.Inject;
import net.twerion.hungergames.arena.Arena;
import org.bukkit.Location;

public final class ArenaSourceTierProvider implements TierProvider {
  private Arena arena;

  @Inject
  private ArenaSourceTierProvider(
    Arena arena
  ) {
    this.arena = arena;
  }

  @Override
  public float tierAtLocation(Location location) {
    if (arena.tierTwoLootCrates().contains(location)) {
      return 2;
    }
    if (arena.lootDropPositions().contains(location)) {
      return 3;
    }
    return 1;
  }
}

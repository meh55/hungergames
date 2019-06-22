package net.twerion.hungergames.loot;

import com.google.inject.Inject;
import net.twerion.hungergames.arena.Arena;
import net.twerion.hungergames.border.Border;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaSourceLootDropProvider implements LocationSource {
  private Arena arena;
  private Border border;

  @Inject
  private ArenaSourceLootDropProvider(
    Arena arena,
    Border border
  ) {
    this.border = border;
    this.arena = arena;
  }

  @Override
  @Nullable
  public Location next() {
    List<Location> lootDropPositions = new ArrayList<>(arena.lootDropPositions());
    Collections.shuffle(lootDropPositions);
    for (Location location : lootDropPositions) {
      if (isInsideBorder(location)) {
        return location;
      }
    }
    return null;
  }

  private boolean isInsideBorder(Location location) {
    Location center = border.worldBorder().getCenter();
    double distance = location.distance(center);
    return distance < border.worldBorder().getSize() * 2;
  }
}

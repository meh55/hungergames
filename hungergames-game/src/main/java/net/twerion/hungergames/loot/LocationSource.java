package net.twerion.hungergames.loot;

import org.bukkit.Location;

import javax.annotation.Nullable;

public interface LocationSource {
  @Nullable
  Location next();
}

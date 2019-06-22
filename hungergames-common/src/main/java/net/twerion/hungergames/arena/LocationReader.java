package net.twerion.hungergames.arena;

import net.twerion.hungergames.Preconditions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;

public final class LocationReader {
  private World world;

  private LocationReader(World world) {
    this.world = world;
  }

  public Location read(ConfigurationSection yaml) {
    double x = yaml.getDouble("x", 0);
    double y = yaml.getDouble("y", 0);
    double z = yaml.getDouble("z", 0);
    double yaw = yaml.getDouble("yaw", 0);
    double pitch = yaml.getDouble("pitch", 0);
    return new Location(
      world,
      x,
      y,
      z,
      (float) yaw,
      (float) pitch
    );
  }

  private double readDouble(@Nullable String value, double fallback) {
    if (value == null) {
      return fallback;
    }
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException invalidFormat) {
      return fallback;
    }
  }

  public static LocationReader withWorld(World world) {
    Preconditions.checkNotNull(world);
    return new LocationReader(world);
  }
}

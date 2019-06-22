package net.twerion.hungergames.arena;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public final class LocationWriter {
  private LocationWriter() {}

  public void write(Location location, ConfigurationSection target) {
    target.set("x", location.getX());
    target.set("y", location.getY());
    target.set("z", location.getZ());
    target.set("yaw", location.getYaw());
    target.set("pitch", location.getPitch());
    target.set("world", location.getWorld().getName());
  }

  public static LocationWriter create() {
    return new LocationWriter();
  }
}

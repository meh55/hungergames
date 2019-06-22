package net.twerion.hungergames.arena;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Logger;

public final class ArenaReader {
  private static Logger LOG =
    Logger.getLogger(ArenaReader.class.getSimpleName());

  private YamlConfiguration yaml;
  private Arena.Builder builder;
  private LocationReader locationReader;

  private ArenaReader(YamlConfiguration yaml, Arena.Builder builder) {
    this.yaml = yaml;
    this.builder = builder;
  }

  public Arena readArena() {
    String world = yaml.getString("worldName");
    if (world == null) {
      LOG.severe("Could not read 'world'.");
      throw new IllegalStateException();
    }
    this.locationReader = LocationReader.withWorld(Bukkit.getWorld(world));

    readSpawnRing();
    readTierTwoCratePositions();
    readLootCrateDropPositions();
    String name = yaml.getString("name", "undefined");
    String author = yaml.getString("author", "undefined");
    return builder
      .withName(name)
      .withAuthor(author)
      .withWorldName(world)
      .create();
  }

  private void readTierTwoCratePositions() {
    int listSize = yaml.getInt("tierTwoLootCrateCount");
    ConfigurationSection section = yaml.getConfigurationSection("tierTwoLootCrates");
    if (section == null) {
      LOG.warning("Could not read 'tierTwoLootCrates'");
      return;
    }
    for (int index = 0; index < listSize; index++) {
      ConfigurationSection location = section.getConfigurationSection(String.valueOf(index));
      System.out.println(location.contains("x"));
      Location position = locationReader.read(location);
      builder.addTierTwoLootCrate(position);
    }
  }

  private void readLootCrateDropPositions() {
    int listSize = yaml.getInt("lootDropPositionCount");
    ConfigurationSection section = yaml.getConfigurationSection("lootDropPositions");
    if (section == null) {
      LOG.warning("Could not read 'lootDropPositions'");
      return;
    }
    for (int index = 0; index < listSize; index++) {
      ConfigurationSection location = section.getConfigurationSection(String.valueOf(index));
      Location position = locationReader.read(location);
      builder.addLootDropPosition(position);
    }
  }

  private void readSpawnRing() {
    int listSize = yaml.getInt("spawnRingSize");
    ConfigurationSection section = yaml.getConfigurationSection("spawnRing");
    if (section == null) {
      LOG.warning("Could not read 'spawnRing'");
      return;
    }
    for (int index = 0; index < listSize; index++) {
      ConfigurationSection location = section.getConfigurationSection(String.valueOf(index));
      Location position = locationReader.read(location);
      builder.addSpawnPoint(position);
    }
  }

  public static ArenaReader of(YamlConfiguration yaml) {
    Preconditions.checkNotNull(yaml);
    return new ArenaReader(yaml, Arena.newBuilder());
  }
}

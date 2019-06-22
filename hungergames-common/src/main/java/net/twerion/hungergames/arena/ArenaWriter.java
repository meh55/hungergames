package net.twerion.hungergames.arena;

import net.twerion.hungergames.Preconditions;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class ArenaWriter {
  private Arena arena;
  private YamlConfiguration yaml;
  private LocationWriter locationWriter;

  private ArenaWriter(Arena arena, YamlConfiguration yaml, LocationWriter locationWriter) {
    this.arena = arena;
    this.yaml = yaml;
    this.locationWriter = locationWriter;
  }

  public void write() {
    writeLootCrates();
    writeTierTwoLootCrates();
    writeSpawnRing();
    yaml.set("name", arena.name());
    yaml.set("author", arena.author());
    yaml.set("worldName", arena.worldName());
  }

  private void writeLootCrates() {
    ConfigurationSection lootCrates = yaml.createSection("lootDropPositions");
    yaml.set("lootDropPositionCount", arena.lootDropPositions().size());
    int index = 0;
    for (Location lootDropPosition : arena.lootDropPositions()) {

      ConfigurationSection location = lootCrates.createSection(String.valueOf(index));
      locationWriter.write(lootDropPosition, location);
      index++;
    }
  }

  private void writeTierTwoLootCrates() {
     ConfigurationSection lootCrates = yaml.createSection("tierTwoLootCrates");
    yaml.set("tierTwoLootCrateCount", arena.tierTwoLootCrates().size());
    int index = 0;
    for (Location tierTwoLootCrate : arena.tierTwoLootCrates()) {
      ConfigurationSection location = lootCrates.createSection(String.valueOf(index));
      locationWriter.write(tierTwoLootCrate, location);
      index++;
    }
  }

  private void writeSpawnRing() {
    ConfigurationSection spawnRing = yaml.createSection("spawnRing");
    yaml.set("spawnRingSize", arena.spawnRing().size());
    int index = 0;
    for (Location spawnPoint : arena.spawnRing()) {
      ConfigurationSection location = spawnRing.createSection(String.valueOf(index));
      locationWriter.write(spawnPoint, location);
      index++;
    }
  }

  public static ArenaWriter create(Arena arena, YamlConfiguration target) {
    Preconditions.checkNotNull(arena);
    Preconditions.checkNotNull(target);
    return new ArenaWriter(arena, target, LocationWriter.create());
  }
}

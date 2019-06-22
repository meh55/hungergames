package net.twerion.hungergames.arena;

import com.google.common.collect.ImmutableList;
import net.twerion.hungergames.Preconditions;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public final class Arena {
  private String name;
  private String author;
  private String worldName;
  private Collection<Location> spawnRing;
  private Collection<Location> lootDropPositions;
  private Collection<Location> tierTwoLootCrates;

  private Arena(
    String name,
    String author,
    String worldName,
    Collection<Location> spawnRing,
    Collection<Location> lootDropPositions,
    Collection<Location> tierTwoLootCrates
  ) {
    this.name = name;
    this.author = author;
    this.worldName = worldName;
    this.spawnRing = spawnRing;
    this.lootDropPositions = lootDropPositions;
    this.tierTwoLootCrates = tierTwoLootCrates;
  }

  public String name() {
    return this.name;
  }

  public String author() {
    return this.author;
  }

  public String worldName() {
    return this.worldName;
  }

  public Collection<Location> spawnRing() {
    return ImmutableList.copyOf(this.spawnRing);
  }


  public Collection<Location> lootDropPositions() {
    return ImmutableList.copyOf(this.lootDropPositions);
  }

  public Collection<Location> tierTwoLootCrates() {
    return ImmutableList.copyOf(this.tierTwoLootCrates);
  }

  public static Builder newBuilder() {
    return new Builder(new Arena(
      "undefined",
      "undefined",
      "world",
      new ArrayList<>(),
      new ArrayList<>(),
      new ArrayList<>()
    ));
  }
  public static Builder newBuilder(Arena arena) {
    Preconditions.checkNotNull(arena);
    return new Builder(new Arena(
      arena.name,
      arena.author,
      arena.worldName,
      new ArrayList<>(arena.spawnRing),
      new ArrayList<>(arena.lootDropPositions),
      new ArrayList<>(arena.tierTwoLootCrates)
    ));
  }

  public static final class Builder {
    private Arena prototype;

    private Builder(Arena arena) {
      this.prototype = arena;
    }

    public Builder withWorldName(String worldName) {
      Preconditions.checkNotNull(worldName);
      prototype.worldName = worldName;
      return this;
    }

    public Builder withName(String name) {
      Preconditions.checkNotNull(name);
      prototype.name = name;
      return this;
    }

    public Builder withAuthor(String name) {
      Preconditions.checkNotNull(name);
      prototype.author = name;
      return this;
    }

    public Builder addSpawnPoint(Location spawnPoint) {
      Preconditions.checkNotNull(spawnPoint);
      prototype.spawnRing.add(spawnPoint);
      return this;
    }

    public Builder withSpawnRing(Collection<Location> spawnRing) {
      Preconditions.checkNotNull(spawnRing);
      prototype.spawnRing = new ArrayList<>(spawnRing);
      return this;
    }

    public Builder addLootDropPosition(Location lootDropPosition) {
      Preconditions.checkNotNull(lootDropPosition);
      prototype.lootDropPositions.add(lootDropPosition);
      return this;
    }

    public Builder withLootDropPositions(Collection<Location> lootDropPositions) {
      Preconditions.checkNotNull(lootDropPositions);
      prototype.lootDropPositions = new ArrayList<>(lootDropPositions);
      return this;
    }

    public Builder addTierTwoLootCrate(Location tierTwoLootCrate) {
      Preconditions.checkNotNull(tierTwoLootCrate);
      prototype.tierTwoLootCrates.add(tierTwoLootCrate);
      return this;
    }

    public Builder withTierTwoLootCrate(Location tierTwoLootCrate) {
      Preconditions.checkNotNull(tierTwoLootCrate);
      prototype.tierTwoLootCrates.add(tierTwoLootCrate);
      return this;
    }

    public Arena create() {
      return new Arena(
        prototype.name,
        prototype.author,
        prototype.worldName,
        new HashSet<>(prototype.spawnRing),
        new HashSet<>(prototype.lootDropPositions),
        new HashSet<>(prototype.tierTwoLootCrates)
      );
    }
  }
}

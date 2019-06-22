package net.twerion.hungergames.game;

import net.twerion.hungergames.arena.Arena;
import net.twerion.hungergames.arena.ArenaReader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.logging.Logger;

public final class ArenaLoader {
  private static final Logger LOG = Logger.getLogger(ArenaLoader.class.getSimpleName());
  private static final String ARENA_FILE_NAME_SUFFIX = "_setup.yml";

  private ArenaLoader() {}

  public Arena loadArena(String name, Path directory) {
    String fileName = name.concat(ARENA_FILE_NAME_SUFFIX);
    Path filePath = directory.getParent().resolve(fileName);
    try (Reader reader = Files.newBufferedReader(filePath)) {
      ArenaReader arenaReader = ArenaReader.of(
        YamlConfiguration.loadConfiguration(reader)
      );
      return arenaReader.readArena();
    } catch (IOException ioFailure) {
      LOG.severe("Could not load arena");
      LOG.severe(ioFailure.getMessage());
      throw new IllegalStateException();
    }
  }

  public static ArenaLoader create() {
    return new ArenaLoader();
  }
}

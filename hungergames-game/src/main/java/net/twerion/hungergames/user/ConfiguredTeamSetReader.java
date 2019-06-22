package net.twerion.hungergames.user;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfiguredTeamSetReader implements Provider<ConfiguredTeamSet> {
  private static final Logger LOG = Logger.getLogger(
    ConfiguredTeamSetReader.class.getSimpleName()
  );

  private FileConfiguration configuration;

  @Inject
  private ConfiguredTeamSetReader(FileConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public ConfiguredTeamSet get() {
    Map<UUID, ConfiguredTeam> teams = new HashMap<>();
    ConfigurationSection teamsSection = configuration.getConfigurationSection("teams");
    if (teamsSection == null) {
      LOG.warning("Could not read list of teams");
      return ConfiguredTeamSet.of(teams);
    }
    for (String key : teamsSection.getKeys(false)) {
      ConfigurationSection teamSection = teamsSection.getConfigurationSection(key);
      ConfiguredTeam team = readConfiguredTeam(teamSection);
      team.playerIds().forEach(
        id -> teams.put(id, team)
      );
    }
    return ConfiguredTeamSet.of(teams);
  }

  private ConfiguredTeam readConfiguredTeam(ConfigurationSection section) {
    String id = section.getString("id");
    Collection<UUID> ids = section.getStringList("members")
      .stream()
      .map(UUID::fromString)
      .collect(Collectors.toSet());

    return ConfiguredTeam.create(id, ids);
  }
}

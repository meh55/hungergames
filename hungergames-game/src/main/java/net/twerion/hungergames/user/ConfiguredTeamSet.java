package net.twerion.hungergames.user;

import net.twerion.hungergames.Preconditions;

import java.util.*;
import java.util.stream.Collectors;

public final class ConfiguredTeamSet {
  private Map<UUID, ConfiguredTeam> teams;

  private ConfiguredTeamSet(
    Map<UUID, ConfiguredTeam> teams
  ) {
    this.teams = teams;
  }

  public Optional<ConfiguredTeam> teamOfPlayer(UUID id) {
    Preconditions.checkNotNull(id);
    return Optional.ofNullable(teams.get(id));
  }

  public Collection<ConfiguredTeam> teams() {
    return teams.values().stream().distinct().collect(Collectors.toList());
  }

  public static ConfiguredTeamSet of(Map<UUID, ConfiguredTeam> teams) {
    Preconditions.checkNotNull(teams);
    return new ConfiguredTeamSet(new HashMap<>(teams));
  }
}

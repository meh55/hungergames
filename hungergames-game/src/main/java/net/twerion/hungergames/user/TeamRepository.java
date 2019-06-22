package net.twerion.hungergames.user;

import java.util.*;

public final class TeamRepository {
  private ConfiguredTeamSet configuredTeamSet;
  private Map<String, Team> teams;

  private TeamRepository(
    ConfiguredTeamSet configuredTeamSet,
    Map<String, Team> teams
  ) {
    this.configuredTeamSet = configuredTeamSet;
    this.teams = teams;
  }

  public Optional<Team> getInstance(UUID player) {
    return configuredTeamSet.teamOfPlayer(player)
      .map(ConfiguredTeam::teamId)
      .map(this::getOrCreate);
  }

  private Team getOrCreate(String id) {
    return teams.computeIfAbsent(id, key -> Team.of(id, new ArrayList<>()));
  }

  public static TeamRepository create(
    ConfiguredTeamSet configuredTeamSet
  ) {
    return new TeamRepository(configuredTeamSet, new HashMap<>());
  }
}

package net.twerion.hungergames.user;

import net.twerion.hungergames.Preconditions;
import sun.security.krb5.Config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public final class ConfiguredTeam {
  private String teamId;
  private Collection<UUID> playerIds;

  private ConfiguredTeam(String teamId, Collection<UUID> playerIds) {
    this.teamId = teamId;
    this.playerIds = playerIds;
  }

  public boolean contains(UUID id) {
    return playerIds.contains(id);
  }

  public String teamId() {
    return teamId;
  }

  public Collection<UUID> playerIds() {
    return new HashSet<>(playerIds);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof ConfiguredTeam)) {
      return false;
    }
    ConfiguredTeam team = ((ConfiguredTeam) other);
    return teamId.equals(team.teamId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId);
  }

  public static ConfiguredTeam create(
      String teamId, Collection<UUID> playerIds
  ) {
    Preconditions.checkNotNull(teamId);
    Preconditions.checkNotNull(playerIds);
    return new ConfiguredTeam(teamId, playerIds);
  }
}

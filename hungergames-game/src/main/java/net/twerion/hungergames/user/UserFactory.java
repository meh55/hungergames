package net.twerion.hungergames.user;

import com.google.inject.Inject;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class UserFactory {
  private TeamRepository teams;

  @Inject
  private UserFactory(
    TeamRepository teams
  ) {
    this.teams = teams;
  }

  public User getInstance(Player player) {
    Team team = teams.getInstance(player.getUniqueId())
      .orElse(Team.of(player.getName(), new ArrayList<>()));

    User user = new User(
      team,
      player,
      new ReentrantReadWriteLock(),
      Records.newBuilder().create()
    );

    team.addMember(user);
    return user;
  }
}

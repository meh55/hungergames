package net.twerion.hungergames.game;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.twerion.hungergames.user.UserRepository;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import net.twerion.hungergames.Preconditions;
import net.twerion.hungergames.user.Team;
import net.twerion.hungergames.user.User;

public final class Game {
  private GamePhaseContext phaseContext;
  private PluginManager eventChannel;
  private Collection<? extends Player> tributes;
  private Collection<Team> teams;
  private UserRepository users;

  @Inject
  private Game(
      GamePhaseContext phaseContext,
      PluginManager eventChannel,
      UserRepository users
  ) {
    this.phaseContext = phaseContext;
    this.eventChannel = eventChannel;
    this.users = users;
    this.tributes = Lists.newArrayList();
    this.teams = Lists.newArrayList();
  }

  public void initializeTributes(Collection<? extends Player> tributes) {
    Preconditions.checkNotNull(tributes);
    this.tributes = new ArrayList<>(tributes);
    this.teams = tributes.stream()
      .map(users::getInstance)
      .map(User::team)
      .distinct()
      .collect(Collectors.toList());
  }

  public int remainingTeamCount() {
    return teams.size();
  }

  public Optional<Team> anyRemainingTeam() {
    return teams.stream().findAny();
  }

  public Collection<? extends Player> tributes() {
    return new ArrayList<>(tributes);
  }

  public void eliminate(User user, TributeEliminateEvent.Cause cause) {
    eliminate(user, cause, null);
  }

  public void eliminate(User user, TributeEliminateEvent.Cause cause, User killer) {
    tributes.remove(user.player());
    eventChannel.callEvent(TributeEliminateEvent.of(user, cause, killer));
    checkTeamElimination(user.team());
  }


  private void checkTeamElimination(Team team) {
    if (!team.isEliminated()) {
      return;
    }
    eliminateTeam(team);
  }

  public void eliminateTeam(Team team) {
    teams.remove(team);
    System.out.println(team.name());
    for (Team teams : teams) {
      System.out.println(teams.name());
    }
    eventChannel.callEvent(TeamEliminateEvent.of(team));
  }

  public GamePhase phase() {
    return phaseContext.current();
  }

  public void beginInitialPhase(GamePhase phase) {
    phaseContext.begin(phase);
  }

  public void updatePhase(GamePhase target) {
    phaseContext.change(target);
  }
}

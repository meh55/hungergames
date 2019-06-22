package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;
import net.twerion.hungergames.user.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamEliminateEvent extends Event {
  private static final HandlerList handlerList = new HandlerList();

  private Team team;

  private TeamEliminateEvent(Team team) {
    this.team = team;
  }

  public Team team() {
    return team;
  }


  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public static TeamEliminateEvent of(Team team) {
    Preconditions.checkNotNull(team);
    return new TeamEliminateEvent(team);
  }
}

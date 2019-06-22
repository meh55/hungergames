package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.TeamEliminateEvent;
import net.twerion.hungergames.game.cooldown.CooldownPhaseStarter;
import net.twerion.hungergames.locale.MessageStore;
import net.twerion.hungergames.user.Team;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

@Component
@PhaseBound(IngamePhase.class)
public class WinCheck implements Listener {
  private static final Logger LOG = Logger.getLogger(WinCheck.class.getSimpleName());
  private Game game;
  private MessageStore messages;
  private CooldownPhaseStarter cooldownPhaseStarter;

  @Inject
  private WinCheck(
      Game game, MessageStore messages, CooldownPhaseStarter cooldownPhaseStarter
  ) {
    this.game = game;
    this.messages = messages;
    this.cooldownPhaseStarter = cooldownPhaseStarter;
  }

  @EventHandler
  public void checkForWinningTeam(TeamEliminateEvent elimination) {
    if (game.remainingTeamCount() == 0) {
      LOG.severe("There is no remaining team after an elimination in IngamePhase");
      return;
    }
    if (game.remainingTeamCount() == 1) {
      Team remaining = game.anyRemainingTeam().orElseThrow(IllegalAccessError::new);
      cooldownPhaseStarter.start(game);
      Bukkit.broadcastMessage(messages.find("message.team.win", remaining.name()));
      remaining.win();
    }
  }
}

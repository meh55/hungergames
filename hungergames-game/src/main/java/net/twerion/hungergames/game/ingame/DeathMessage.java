package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.TeamEliminateEvent;
import net.twerion.hungergames.game.TributeEliminateEvent;
import net.twerion.hungergames.locale.MessageStore;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Component
@PhaseBound(
  IngamePhase.class
)
public class DeathMessage implements Listener {
  private MessageStore messages;

  @Inject
  private DeathMessage(MessageStore messages) {
    this.messages = messages;
  }

  @EventHandler
  public void broadcastElimination(TributeEliminateEvent elimination) {
    String tributeName = elimination.tribute().player().getDisplayName();
    if (elimination.cause() == TributeEliminateEvent.Cause.KILLED) {
      if (elimination.killer().isPresent()) {
          String killerName =
            elimination.killer().get().player().getDisplayName();
        Bukkit.broadcastMessage(
          messages.find("message.tribute.kill", tributeName, killerName));
        return;
      }
    }
    Bukkit.broadcastMessage(messages.find("message.tribute.quit", tributeName));
  }

  @EventHandler
  public void broadcastTeamElimination(TeamEliminateEvent elimination) {
    Bukkit.broadcastMessage(
      messages.find("message.team.elimination", elimination.team().name())
    );
  }
}

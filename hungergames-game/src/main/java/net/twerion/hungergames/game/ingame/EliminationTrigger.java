package net.twerion.hungergames.game.ingame;

import javax.annotation.Nullable;

import com.google.inject.Inject;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.event.PlayerDisconnectEvent;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.TributeEliminateEvent;
import net.twerion.hungergames.user.User;
import net.twerion.hungergames.user.UserRepository;

@Component
@PhaseBound(IngamePhase.class)
public class EliminationTrigger implements Listener {
  private Game game;
  private PluginManager eventChannel;
  private UserRepository userRepository;

  @Inject
  private EliminationTrigger(
      Game game,
      PluginManager eventChannel,
      UserRepository userRepository
  ) {
    this.game = game;
    this.userRepository = userRepository;
    this.eventChannel = eventChannel;
  }

  @EventHandler
  public void eliminateDeathPlayer(PlayerDeathEvent death) {
    User victim = userRepository.getInstance(death.getEntity());
    if (!victim.isAlive()) {
      return;
    }
    reportElimination(victim, TributeEliminateEvent.Cause.DIED);
  }

  @EventHandler
  public void eliminateLeavingPlayer(PlayerDisconnectEvent disconnect) {
    User user = userRepository.getInstance(disconnect.player());
    if (!user.isAlive()) {
      return;
    }
    reportElimination(user, TributeEliminateEvent.Cause.DISCONNECT);
  }

  private void reportElimination(
      User user, TributeEliminateEvent.Cause fallbackCause
  ) {
    User killer = findKiller(user);
    if (killer == null) {
      game.eliminate(user, fallbackCause);
    } else {
      game.eliminate(user, TributeEliminateEvent.Cause.KILLED, killer);
    }
  }

  @Nullable
  private User findKiller(User victim) {
    Player player = victim.player();
    if (player.getKiller() != null) {
      return userRepository
        .getExistingInstance(player.getKiller())
        .orElse(null);
    }
    Player lastDamage = victim.lastDamager();
    if (lastDamage != null) {
      return userRepository.getExistingInstance(lastDamage)
        .orElse(null);
    }
    return null;
  }
}

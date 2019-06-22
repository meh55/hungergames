package net.twerion.hungergames.game.waiting;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.AbstractGamePhase;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.locale.MessageStore;

import javax.inject.Named;

@Component
@PhaseBound(WaitingPhase.class)
public final class WaitingPhase extends AbstractGamePhase {
  private Location spawnPoint;

  @Inject
  private WaitingPhase(
    @PhaseBound(WaitingPhase.class) GamePhaseConfig config,
    @Named("spawnPoint") Location spawnPoint
  ) {
    super(config);
    this.spawnPoint = spawnPoint;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void applyPlayerStateToJoiningPlayer(PlayerJoinEvent join) {
    config().playerState().apply(join.getPlayer());
    join.getPlayer().teleport(spawnPoint);
  }
}

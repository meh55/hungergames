package net.twerion.hungergames.game.warmup;

import com.google.inject.Inject;
import net.twerion.hungergames.game.AbstractGamePhase;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.locale.MessageStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

public final class WarmupPhase extends AbstractGamePhase  {

  private MessageStore messages;

  @Inject
  private WarmupPhase(
    @PhaseBound(WarmupPhase.class)GamePhaseConfig config,
    MessageStore messages
  ) {
    super(config);
    this.messages = messages;
  }

  @EventHandler
  public void blockLogin(PlayerLoginEvent login) {
    login.disallow(PlayerLoginEvent.Result.KICK_OTHER, messages.find("warmup.kick"));
  }
}

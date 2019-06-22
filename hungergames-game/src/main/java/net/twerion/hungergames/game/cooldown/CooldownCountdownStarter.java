package net.twerion.hungergames.game.cooldown;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.countdown.Countdown;
import net.twerion.hungergames.game.GamePhaseBeginEvent;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.warmup.WarmupPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

@Component
@PhaseBound(CooldownPhase.class)
public class CooldownCountdownStarter implements Listener {
  private Plugin plugin;
  private Countdown countdown;

  @Inject
  private CooldownCountdownStarter(
      Plugin plugin,
      @PhaseBound(CooldownPhase.class)Countdown countdown
  ) {
    this.plugin = plugin;
    this.countdown = countdown;
  }

  @EventHandler
  public void startWarmupCounter(GamePhaseBeginEvent begin) {
    countdown.start(plugin);
  }
}

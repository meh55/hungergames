package net.twerion.hungergames.game.warmup;

import com.google.inject.Inject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.countdown.Countdown;
import net.twerion.hungergames.game.GamePhaseBeginEvent;
import net.twerion.hungergames.game.PhaseBound;

@Component
@PhaseBound(WarmupPhase.class)
public class WarmupCountdownStarter implements Listener {
  private Plugin plugin;
  private Countdown countdown;

  @Inject
  private WarmupCountdownStarter(
      Plugin plugin,
      @PhaseBound(WarmupPhase.class)Countdown countdown
  ) {
    this.plugin = plugin;
    this.countdown = countdown;
  }

  @EventHandler
  public void startWarmupCounter(GamePhaseBeginEvent begin) {
    countdown.start(plugin);
  }
}
package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.border.Border;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseBeginEvent;
import net.twerion.hungergames.game.PhaseBound;

import net.twerion.hungergames.loot.SupplyDropSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

@Component
@PhaseBound(IngamePhase.class)
public final class BorderStarter implements Listener {
  private Border border;
  private Game game;
  private Plugin plugin;
  private SupplyDropSpawner supplyDropSpawner;

  @Inject
  private BorderStarter(Game game, Border border, Plugin plugin, SupplyDropSpawner supplyDropSpawner) {
    this.game = game;
    this.border = border;
    this.plugin = plugin;
    this.supplyDropSpawner = supplyDropSpawner;
  }

  @EventHandler
  public void startBorderOnBegin(GamePhaseBeginEvent begin) {
    border.configure();
    border.startShrinking(game, plugin);
    supplyDropSpawner.startTimer();
  }
}
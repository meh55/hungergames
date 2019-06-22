package net.twerion.hungergames.game.warmup;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.arena.Arena;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseBeginEvent;
import net.twerion.hungergames.game.PhaseBound;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Component
@PhaseBound(WarmupPhase.class)
public class PlayerTeleporter implements Listener {
  private int index;
  private Game game;
  private List<Location> spawnPoints;

  @Inject
  private PlayerTeleporter(Arena arena, Game game) {
    this.index = 0;
    this.game = game;
    this.spawnPoints = ImmutableList.copyOf(arena.spawnRing());
  }

  @EventHandler
  public void teleportTributes(GamePhaseBeginEvent begin) {
    for (Player player : game.tributes()) {
      teleport(player);
    }
  }

  public void teleport(Player player) {
    Location location = spawnPoints.get(index);
    player.teleport(location);
    this.index = Math.abs(index + 1 % spawnPoints.size());
  }
}

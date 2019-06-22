package net.twerion.hungergames.loot;

import com.google.inject.Inject;
import net.twerion.hungergames.countdown.Countdown;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhase;
import net.twerion.hungergames.game.ingame.IngamePhase;
import net.twerion.hungergames.locale.MessageStore;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class SupplyDropSpawner {
  private static final Logger LOG = Logger.getLogger(SupplyDropSpawner.class.getSimpleName());
  private LocationSource locationSource;
  private MessageStore messages;
  private Plugin plugin;
  private Game game;

  @Inject
  private SupplyDropSpawner(
    Plugin plugin,
    MessageStore messages,
    LocationSource locationSource,
    Game game
  ) {
    this.game = game;
    this.plugin = plugin;
    this.messages = messages;
    this.locationSource = locationSource;
  }

  public void startTimer() {
    new BukkitRunnable(){
      @Override
      public void run() {
        if (!(game.phase() instanceof IngamePhase)) {
          cancel();
          return;
        }
        schedule();
      }
    }.runTaskTimer(plugin, 30, (5*20*60)+ 10);
  }

  public void schedule() {
    Location next = locationSource.next();
    if (next == null) {
      LOG.warning("No next spawn found");
      return; // There are more locations left
    }
    schedule(next);
  }

  public void schedule(Location location) {
    schedule(location, 5);
  }

  public void schedule(Location location, int timeInMinutes) {
    String locationName=  locationToString(location);
    Countdown countdown = Countdown
      .newBuilder()
      .withTickListener((minutes, seconds) -> {
        if (seconds == 0) {
          switch (minutes) {
            case 5:
            case 3:
            case 2:
              Bukkit.broadcastMessage(messages.find("message.loot.supply.countdown.minutes", locationName, minutes));
              break;
            case 1:
              Bukkit.broadcastMessage(messages.find("message.loot.supply.countdown.minute", locationName));
              break;
          }
        }
        if (minutes == 1) {
          switch (seconds) {
            case 30:
            case 10:
            case 5:
            case 3:
            case 2:
              Bukkit.broadcastMessage(messages.find("message.loot.supply.countdown.seconds", locationName, seconds));
              break;
            case 1:
              Bukkit.broadcastMessage(messages.find("message.loot.supply.countdown.second", locationName));
              break;
          }
        }
      })
      .withCompletionListener(() -> spawn(location))
      .withInitialCount(timeInMinutes, 0)
      .withTargetCount(0)
      .withDirection(Countdown.Direction.DECREMENTING)
      .create();

    countdown.start(plugin);
  }

  public void spawn(Location target) {
    Location spawnPoint = target.add(0, 50, 0);
    FallingBlock fallingBlock = target.getWorld().spawnFallingBlock(
      spawnPoint, Material.WOOD, (byte) 0
    );
    fallingBlock.setHurtEntities(false);
    fallingBlock.setDropItem(false);
    target.getWorld().playEffect(target, Effect.ENDER_SIGNAL, 10, 10);

    String locationName = locationToString(spawnPoint);
    Bukkit.broadcastMessage(messages.find("message.loot.supply.spawn", locationName));
  }

  private String locationToString(Location location) {
    return String.format(
      "x: %d, z: %d", location.getBlockX(), location.getBlockZ()
    );
  }
}

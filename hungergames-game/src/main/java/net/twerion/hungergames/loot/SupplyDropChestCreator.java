package net.twerion.hungergames.loot;

import com.google.inject.Inject;
import net.twerion.hungergames.Component;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collection;
import java.util.HashSet;

@Component
public class SupplyDropChestCreator implements Listener  {
  private Collection<Location> spawnedChests;

  @Inject
  private SupplyDropChestCreator() {
    this.spawnedChests = new HashSet<>();
  }

  @EventHandler
  public void createSupplyCrate(EntityChangeBlockEvent landing) {
    if (landing.getEntityType() != EntityType.FALLING_BLOCK) {
      return;
    }
    landing.setCancelled(true);
    landing.getEntity().remove();

    Location collisionPoint = landing.getBlock().getLocation();
    spawnedChests.add(collisionPoint.clone());
    World world = landing.getEntity().getWorld();
    world.getBlockAt(collisionPoint).setType(Material.CHEST);
    world.playEffect(collisionPoint, Effect.ENDER_SIGNAL, 10, 10);
    world.playSound(collisionPoint, Sound.ZOMBIE_WOODBREAK, 1, 1);
    spawnBeacon(collisionPoint.subtract(0, 1,0 ));
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void removeBeaconOnOpen(PlayerInteractEvent open) {
    if (open.isCancelled()) {
      return;
    }
    if (open.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (open.getClickedBlock() == null || open.getClickedBlock().getType() != Material.CHEST) {
      return;
    }
    if (open.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }
    Location blockPosition = open.getClickedBlock().getLocation();
    if (spawnedChests.remove(blockPosition)) {
      destroyBeacon(blockPosition.subtract(0, 1, 0));
      open.getPlayer().playSound(open.getPlayer().getEyeLocation(), Sound.ENDERDRAGON_GROWL, 1 ,1 );
    }
  }


  private void spawnBeacon(Location location) {
    int x = location.getBlockX();
    int y = location.getBlockY() - 3;
    int z = location.getBlockZ();

    World world = location.getWorld();

    world.getBlockAt(x, y, z).setType(Material.BEACON);
    for (int i = 0; i <= 2; ++i) {
      world.getBlockAt(x, (y + 1) + i, z).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 5, false);// lime
    }
    for (int xPoint = x-1; xPoint <= x+1 ; xPoint++) {
      for (int zPoint = z-1 ; zPoint <= z+1; zPoint++) {
        world.getBlockAt(xPoint, y-1, zPoint).setType(Material.IRON_BLOCK);
      }
    }
  }

  private void destroyBeacon(Location location) {
    location.getWorld().getBlockAt(location.subtract(0, 1, 0))
      .setType(Material.DIAMOND_BLOCK);
  }
}

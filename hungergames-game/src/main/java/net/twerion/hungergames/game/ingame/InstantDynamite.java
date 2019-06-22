package net.twerion.hungergames.game.ingame;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@Component
@PhaseBound(IngamePhase.class)
public class InstantDynamite implements Listener {

  @EventHandler(
    priority = EventPriority.HIGH
  )
  public void instantPrime(BlockPlaceEvent place) {
    if (place.isCancelled()) {
      return;
    }
    if (place.getBlockPlaced().getType() != Material.TNT) {
      return;
    }
    Location location = place.getBlockPlaced().getLocation();
    location.getWorld().spawn(location, TNTPrimed.class);
  }
}

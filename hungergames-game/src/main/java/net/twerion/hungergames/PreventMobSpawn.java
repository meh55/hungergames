package net.twerion.hungergames;

import org.bukkit.entity.Monster;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

@Component
public class PreventMobSpawn implements Listener {

  public void preventMobSpawn(EntitySpawnEvent spawn) {
    if (spawn.getEntity() instanceof Monster) {
      spawn.setCancelled(true);
    }
  }
}

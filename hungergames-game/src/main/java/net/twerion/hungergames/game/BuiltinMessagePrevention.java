package net.twerion.hungergames.game;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.event.PlayerDisconnectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@Component
public final class BuiltinMessagePrevention implements Listener {

  @EventHandler
  public void preventAchievementBroadcast(PlayerAchievementAwardedEvent achievementAward) {
    achievementAward.setCancelled(true);
  }

  @EventHandler(
    priority = EventPriority.LOWEST
  )
  public void preventDefaultJoinMessage(PlayerJoinEvent join) {
    join.setJoinMessage(null);
  }

  @EventHandler(
    priority = EventPriority.LOWEST
  )
  public void preventDefaultQuitMessage(PlayerDisconnectEvent disconnect) {
    disconnect.preventMessage();
  }

  @EventHandler(
    priority = EventPriority.LOWEST
  )
  public void deleteBuiltinDeathMessage(PlayerDeathEvent death) {
    death.setDeathMessage(null);
  }
}

package net.twerion.hungergames.event;

import javax.inject.Inject;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import net.twerion.hungergames.Component;

@Component
public final class DisconnectEventTrigger implements Listener {

  private PluginManager eventChannel;

  @Inject
  private DisconnectEventTrigger(PluginManager eventChannel) {
    this.eventChannel = eventChannel;
  }

  @EventHandler
  public void callDisconnectOnQuit(PlayerQuitEvent quit) {
    PlayerDisconnectEvent disconnect = PlayerDisconnectEvent.of(quit);
    eventChannel.callEvent(disconnect);
    quit.setQuitMessage(disconnect.message().orElse(null));
  }

  @EventHandler
  public void callDisconnectOnKick(PlayerKickEvent kick) {
    PlayerDisconnectEvent disconnect = PlayerDisconnectEvent.of(kick);
    eventChannel.callEvent(disconnect);
    kick.setLeaveMessage(disconnect.message().orElse(null));
  }
}

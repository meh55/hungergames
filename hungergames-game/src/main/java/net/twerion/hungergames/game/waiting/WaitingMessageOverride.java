package net.twerion.hungergames.game.waiting;

import java.text.MessageFormat;

import javax.inject.Inject;

import net.twerion.hungergames.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.twerion.hungergames.event.PlayerDisconnectEvent;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.locale.MessageStore;

@Component
@PhaseBound(WaitingPhase.class)
public class WaitingMessageOverride implements Listener {
  private MessageStore messages;

  @Inject
  private WaitingMessageOverride(MessageStore messages) {
    this.messages = messages;
  }

  @EventHandler
  public void prepareAndTeleportJoiningPlayer(PlayerJoinEvent join) {
    Player player = join.getPlayer();

    messages.findOptional("message.lobby.join")
      .map(message -> MessageFormat.format(message, player.getDisplayName()))
      .ifPresent(join::setJoinMessage);
  }

  @EventHandler
  public void updateQuitMessage(PlayerDisconnectEvent disconnect) {
    Player player = disconnect.player();

    disconnect.preventMessage();
    messages.findOptional("message.lobby.quit")
      .map(message -> MessageFormat.format(message, player.getDisplayName()))
      .ifPresent(disconnect::setMessage);
  }
}

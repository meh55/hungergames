package net.twerion.hungergames.game;

import net.twerion.hungergames.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.bukkit.event.EventPriority.LOWEST;

@Component
public final class TributeChat implements Listener {

  @EventHandler(priority = LOWEST)
  public void changeTributeChatFormat(AsyncPlayerChatEvent chat) {
    chat.setMessage(
      String.format("§7%s§8: §f%s", chat.getPlayer().getDisplayName(), chat.getMessage())
    );
    chat.setFormat("%2$s");
  }
}

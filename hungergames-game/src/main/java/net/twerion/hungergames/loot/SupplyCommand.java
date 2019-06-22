package net.twerion.hungergames.loot;

import com.google.inject.Inject;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.twerion.hungergames.CommandLabel;
import net.twerion.hungergames.CommonMessageKey;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.Restricted;
import net.twerion.hungergames.locale.MessageStore;

import java.util.Set;

@Component
@CommandLabel("supply")
public class SupplyCommand extends Restricted implements CommandExecutor {
  private SupplyDropSpawner spawner;
  private MessageStore messages;

  @Inject
  private SupplyCommand(SupplyDropSpawner spawner, MessageStore messages) {
    this.spawner = spawner;
    this.messages = messages;
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] arguments
  ) {
    if (!isPermissed(sender)) {
      sender.sendMessage(messages.find(CommonMessageKey.RESTRICTED_ACTION));
      return true;
    }
    if (!(sender instanceof Player)) {
      return false;
    }
    Player player = (Player) sender;
    Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
    if (targetBlock == null) {
      sender.sendMessage(messages.find("message.command.supply.noblocktarget"));
      return true;
    }
    spawner.spawn(targetBlock.getLocation());
    return true;
  }
}

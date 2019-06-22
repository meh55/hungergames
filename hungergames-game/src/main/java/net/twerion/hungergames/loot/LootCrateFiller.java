package net.twerion.hungergames.loot;

import com.google.inject.Inject;
import net.twerion.hungergames.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;

@Component
public class LootCrateFiller implements Listener {
  private Collection<Location> filledChests;
  private Loot loot;
  private TierProvider tierProvider;

  @Inject
  private LootCrateFiller(TierProvider tierProvider, Loot loot) {
    this.filledChests = new HashSet<>();
    this.loot = loot;
    this.tierProvider = tierProvider;
  }

  @EventHandler
  public void fillChestIfUnfilled(PlayerInteractEvent interaction) {
    if (interaction.isCancelled()
      || interaction.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (interaction.getClickedBlock() == null
      || interaction.getClickedBlock().getType() != Material.CHEST) {
      return;
    }

    Location blockLocation = interaction.getClickedBlock().getLocation();
    if (filledChests.add(blockLocation)) {
      fillChest(interaction.getClickedBlock());
    }
  }

  private void fillChest(Block block) {
    if (!(block.getState() instanceof Chest)) {
      return;
    }
    Chest chest = (Chest) block.getState();
    float tier = tierProvider.tierAtLocation(block.getLocation());
    chest.getInventory().clear();
    loot.fillInventory(tier, chest.getInventory());
  }
}

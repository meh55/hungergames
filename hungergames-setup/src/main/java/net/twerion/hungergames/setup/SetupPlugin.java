package net.twerion.hungergames.setup;

import net.twerion.hungergames.arena.Arena;
import net.twerion.hungergames.arena.ArenaWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class SetupPlugin extends JavaPlugin implements Listener  {
  private Arena.Builder setup;
  private int spawnPointCount;

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable() {
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] arguments
  ) {
    if (!command.getName().equals("setup")) {
      return true;
    }
    if (!(sender instanceof Player)) {
      return true;
    }
    if (arguments.length == 0) {
      printHelp(sender);
      return true;
    }
    Player player = (Player) sender;
    BiConsumer<Player, String[]> subCommand;
    switch (arguments[0].toLowerCase()) {
      case "create":
        subCommand = this::setupCreate;
        break;
      case "lootdrop":
        subCommand = this::setupLootDrop;
        break;
      case "tiertwo":
        subCommand = this::setupTierTwo;
        break;
      case "addspawn":
        subCommand = this::setupAddSpawn;
        break;
      case "finish":
        subCommand = this::finishSetup;
        break;
      default:
        printHelp(player);
        return true;
    }
    String[] subCommandArguments = new String[arguments.length - 1];
    System.arraycopy(arguments, 1, subCommandArguments, 0, subCommandArguments.length);
    subCommand.accept(player, subCommandArguments);
    return true;
  }

  @EventHandler
  public void markAsTierTwo(PlayerInteractEvent interaction) {
    if (interaction.getItem() == null || interaction.getItem().getType() != Material.GOLD_BLOCK) {
      return;
    }
    if (interaction.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (interaction.getClickedBlock() == null || interaction.getClickedBlock().getType() != Material.CHEST) {
      return;
    }
    if (!ensureSetupExists(interaction.getPlayer())) {
      return;
    }
    setup.addTierTwoLootCrate(interaction.getClickedBlock().getLocation());
    interaction.getPlayer().sendMessage("§aSuccess: §7Marked the chest as a " +
      "Tier-Two " +
      "crate");
    interaction.setCancelled(true);
    interaction.getClickedBlock().setType(Material.ENDER_CHEST);
  }

  @EventHandler
  public void addLootDropPosition(BlockPlaceEvent place) {
    if (place.getBlock().getType() != Material.DIAMOND_BLOCK) {
      return;
    }
    place.setCancelled(true);
    if (!ensureSetupExists(place.getPlayer())) {
      return;
    }
    ensureSetupExists(place.getPlayer());
    setup.addLootDropPosition(place.getBlock().getLocation());
    place.getPlayer().sendMessage("§aSuccess: §7Added a loot-drop position");
    place.getBlockPlaced().setType(Material.BEACON);
  }

  @EventHandler
  public void teleportToBlockMiddleOnSneak(PlayerToggleSneakEvent toggleSneak) {
    if (toggleSneak.isSneaking()) {
      teleportToMiddle(toggleSneak.getPlayer());
    }
  }

  private void finishSetup(Player player, String[] arguments) {
    if(!ensureSetupExists(player)) {
      return;
    }
    player.sendMessage("§aSuccess:§7 Finished the setup");

    Arena arena = setup.create();
    Path path = createPath(arena.name());
    try {
      File file = path.toFile();
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      YamlConfiguration configuration = new YamlConfiguration();
      ArenaWriter writer = ArenaWriter.create(setup.create(), configuration);
      writer.write();
      configuration.save(file);
    } catch (IOException ioFailure) {
      ioFailure.printStackTrace();
    }
  }

  private Path createPath(String mapName) {
    return getDataFolder().toPath().getParent().resolve(mapName + "_setup.yml");
  }

  private boolean ensureSetupExists(Player player) {
    if (setup == null) {
      player.sendMessage("§4Error:§c There is no setup in progress");
      player.sendMessage("§4Run:§7 setup create <name> <author>");
      return false;
    }
    return true;
  }

  private void setupAddSpawn(Player player, String[] arguments) {
    if (!ensureSetupExists(player)) {
      return;
    }
    Location location = player.getLocation();
    setup.addSpawnPoint(location);
    spawnPointCount++;
    player.sendMessage("§aSuccess: §7Spawn point §e"+spawnPointCount+"§7 has " +
      "been set");

    spawnArmorStand(player.getLocation().getBlock().getLocation());
  }

  private void teleportToMiddle(Player player) {
    Vector direction = player.getEyeLocation().getDirection();
    Location block = player.getLocation().getBlock().getLocation();
    block.add(0.5, 0, 0.5);
    block.setDirection(direction);
    player.teleport(block);
  }

  private void spawnArmorStand(Location location) {
    location.setYaw(0);
    location.setPitch(0);
    location.add(0.5, 0, 0.5);
    ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
    armorStand.setSmall(true);
    armorStand.setGravity(false);
    armorStand.setVisible(false);
    armorStand.setBasePlate(false);
    armorStand.setHelmet(new ItemStack(Material.GOLD_BLOCK));
  }

  private void setupTierTwo(Player player, String[] arguments) {
    ItemStack item = namedItem(Material.GOLD_BLOCK, "§aTierTwo");
    player.getInventory().addItem(item);
  }

  private void setupLootDrop(Player player, String[] arguments) {
    ItemStack item = namedItem(Material.DIAMOND_BLOCK, "§aLootDrop");
    player.getInventory().addItem(item);
  }

  private ItemStack namedItem(Material material, String name) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    item.setItemMeta(meta);
    return item;
  }

  private void setupCreate(Player player, String[] arguments) {
    if (arguments.length < 2) {
      player.sendMessage("§4Error:§c setup create <name> <author>");
      return;
    }
    String name = arguments[0];
    String author = arguments[1];
    this.setup = Arena.newBuilder()
      .withName(name)
      .withAuthor(author);

    this.spawnPointCount = 0;
    player.sendMessage("§aSuccess: §7Setup has been created");
  }

  private void printHelp(CommandSender sender) {
    sender.sendMessage("§bHere is how to use the 'setup' command:");
    sender.sendMessage("§3setup §bcreate <name> <author>");
    sender.sendMessage("§3setup §bfinish");
    sender.sendMessage("§3setup §blootDrop");
    sender.sendMessage("§3setup §baddSpawn");
    sender.sendMessage("§3setup §btierTwo");
  }
}

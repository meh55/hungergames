package net.twerion.hungergames.game.rule;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.Game;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import javax.inject.Inject;

@Component
public class RuleEnforcer implements Listener {
  private Game game;

  @Inject
  private RuleEnforcer(Game game) {
    this.game = game;
  }


  @EventHandler(priority = EventPriority.LOWEST)
  public void disableMove(PlayerMoveEvent move) {
    boolean disabled = rules().isDisabled(CommonGameRule.MOVE);
    if (disabled && !isOnlyRotation(move)) {
      move.setTo(move.getFrom());
    }
  }

  private boolean isOnlyRotation(PlayerMoveEvent move) {
    Location origin = move.getFrom();
    Location target = move.getTo();
    return target.getX() == origin.getX()
      // && target.getY() == origin.getY() (ENABLE VERTICAL MOVE)
      && target.getZ() == origin.getZ();
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableHunger(FoodLevelChangeEvent foodLevelChange) {
    boolean disabled = rules().isDisabled(CommonGameRule.HUNGER);
    foodLevelChange.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableEntityDamage(EntityDamageEvent damage) {
    boolean disabled = rules().isDisabled(CommonGameRule.ENTITY_DAMAGE);
    damage.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableBlockDestruction(BlockBreakEvent blockBreak) {
    boolean disabled = rules().isDisabled(CommonGameRule.BLOCK_BREAK);
    blockBreak.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableBlockPlacement(BlockPlaceEvent blockPlacement) {
    boolean disabled = rules().isDisabled(CommonGameRule.BLOCK_PLACEMENT);
    blockPlacement.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disablePhysicalInteraction(PlayerInteractEvent interaction) {
    if (interaction.getAction().equals(Action.PHYSICAL)) {
      boolean disabled = rules().isDisabled(CommonGameRule.PHYSICAL_INTERACTION);
      interaction.setCancelled(disabled);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableWeatherChange(WeatherChangeEvent weatherChange) {
    boolean disabled = rules().isDisabled(CommonGameRule.WEATHER_CHANGE);
    weatherChange.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableExplosion(EntityExplodeEvent explosion) {
    boolean disabled = rules().isDisabled(CommonGameRule.ENTITY_EXPLOSION);
    explosion.setCancelled(disabled);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void disableItemDrop(PlayerDropItemEvent drop) {
    boolean disabled = rules().isDisabled(CommonGameRule.ITEM_DROP);
    drop.setCancelled(disabled);
  }

  private CommonGameRuleSet rules() {
    return game.phase().config().rules();
  }
}

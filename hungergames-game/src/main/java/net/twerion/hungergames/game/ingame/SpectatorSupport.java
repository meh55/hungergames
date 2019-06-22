package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.TributeEliminateEvent;
import net.twerion.hungergames.user.PlayerState;
import net.twerion.hungergames.user.User;
import net.twerion.hungergames.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

@Component
@PhaseBound(IngamePhase.class)
public final class SpectatorSupport implements Listener {
  private UserRepository users;
  private PlayerState spectatorState;

  @Inject
  private SpectatorSupport(
      UserRepository users,
      @Named("spectator") PlayerState spectatorState
  ) {
    this.users = users;
    this.spectatorState = spectatorState;
  }

  private boolean isSpectator(Player player) {
    return !users.getInstance(player).isAlive();
  }

  @EventHandler(priority = EventPriority.LOW)
  public void preventSpectatorDamage(EntityDamageEvent damage) {
    if (damage.isCancelled()) {
      return;
    }
    if (!(damage.getEntity() instanceof Player)) {
      return;
    }
    Player damaged = ((Player) damage.getEntity());
    User user = users.getInstance(damaged);
    if (!user.isAlive()) {
      damage.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void preventSpectatorFromDamaging(EntityDamageByEntityEvent damage) {
    if (damage.isCancelled()) {
      return;
    }
    if (!(damage.getDamager() instanceof Player)) {
      return;
    }
    User damager = users.getInstance((Player)damage.getDamager());
    if (!damager.isAlive()) {
      damage.setCancelled(true);
    }
  }

  @EventHandler
  public void applySpectatorStateToEliminatedPlayer(
    TributeEliminateEvent elimination
  ) {
    if (elimination.cause() != TributeEliminateEvent.Cause.DISCONNECT) {
      elimination.tribute().markAsSpectator();
      elimination.tribute().hideGlobally();
      spectatorState.apply(elimination.tribute().player());
      elimination.tribute().applySpectatorVisibility(users);
    }
  }

  @EventHandler
  public void addJoiningPlayerToSpectatorMode(PlayerJoinEvent join) {
    join.setJoinMessage(null);

    Player joined = join.getPlayer();
    User user = users.getInstance(joined);
    user.markAsSpectator();
    spectatorState.apply(user.player());
    user.applySpectatorVisibility(users);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void preventSpectatorFromDroppingItems(PlayerDropItemEvent drop) {
    if (drop.isCancelled()) {
      return;
    }
    if (isSpectator(drop.getPlayer())) {
      drop.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void preventSpectatorFromPickingUpItems(PlayerPickupItemEvent pickup) {
    if (pickup.isCancelled()) {
      return;
    }
    if (isSpectator(pickup.getPlayer())) {
      pickup.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void preventSpectatorFromInteracting(PlayerInteractEvent interaction) {
    if (interaction.isCancelled()) {
      return;
    }
    if (isSpectator(interaction.getPlayer())) {
      interaction.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void preventSpectatorFromBeingTargeted(EntityTargetEvent target) {
    if (target.isCancelled()) {
      return;
    }
    if (!(target.getTarget() instanceof Player)) {
      return;
    }
    if (isSpectator((Player) target.getTarget())) {
      target.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void preventSpectatorFromLoosingFoodLevels(FoodLevelChangeEvent foodLevelChange) {
    if (foodLevelChange.isCancelled()) {
      return;
    }
    if (isSpectator((Player)foodLevelChange.getEntity())) {
      foodLevelChange.setCancelled(true);
    }
  }
}

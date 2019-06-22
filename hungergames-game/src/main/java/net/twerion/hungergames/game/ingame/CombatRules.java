package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.user.User;
import net.twerion.hungergames.user.UserRepository;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Component
@PhaseBound(IngamePhase.class)
public final class CombatRules implements Listener {
  private UserRepository users;

  @Inject
  private CombatRules(UserRepository users) {
    this.users = users;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void updateLastDamager(EntityDamageByEntityEvent damage) {
    if (damage.isCancelled()) {
      return;
    }
    if (!(damage.getEntity() instanceof Player)) {
      return;
    }
    if (!(damage.getDamager() instanceof Player)) {
      return;
    }
    Player damager = findDamager(damage);
    if (damager == null) {
      return;
    }
    User damaged = users.getInstance((Player) damage.getEntity());
    damaged.setLastDamager((Player) damage.getDamager());
  }

  private Player findDamager(EntityDamageByEntityEvent damage) {
    if (damage.getDamager() instanceof Player) {
      return (Player)damage.getDamager();
    }
    if (!(damage.getDamager() instanceof Projectile)) {
      return null;
    }
    Projectile projectile = (Projectile) damage.getDamager();
    return projectile.getShooter() instanceof Player
      ? (Player) projectile.getShooter()
      : null;
  }
}

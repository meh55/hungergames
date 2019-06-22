package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.user.User;
import net.twerion.hungergames.user.UserRepository;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Component
@PhaseBound(IngamePhase.class)
public final class FriendlyDamage implements Listener {
  private UserRepository users;

  @Inject
  private FriendlyDamage(UserRepository users) {
    this.users = users;
  }

  @EventHandler
  public void disableFriendlyDamage(EntityDamageByEntityEvent damage) {
    if (damage.isCancelled()) {
      return;
    }
    if (!(damage.getDamager() instanceof Player)
      || !(damage.getEntity() instanceof Player)) {
      return;
    }
    User damaging = users.getInstance((Player) damage.getDamager());
    if (damaging.team().contains(damage.getEntity().getUniqueId())) {
      damage.setCancelled(true);
    }
  }
}

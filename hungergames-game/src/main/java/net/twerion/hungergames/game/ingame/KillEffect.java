package net.twerion.hungergames.game.ingame;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.TributeEliminateEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

@Component
@PhaseBound(IngamePhase.class)
public final class KillEffect implements Listener {

  public void playKillEffect(TributeEliminateEvent elimination) {
    if (!elimination.killer().isPresent()) {
      return;
    }
    Player killer = elimination.killer().get().player();
    killer.playSound(killer.getEyeLocation(), Sound.LEVEL_UP, 1, 1);
  }
}

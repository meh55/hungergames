package net.twerion.hungergames.game.cooldown;

import com.google.inject.Inject;
import net.twerion.hungergames.game.AbstractGamePhase;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;

public final class CooldownPhase extends AbstractGamePhase {
  @Inject
  private CooldownPhase(
      @PhaseBound(CooldownPhase.class)GamePhaseConfig config
  ) {
     super(config);
  }
}

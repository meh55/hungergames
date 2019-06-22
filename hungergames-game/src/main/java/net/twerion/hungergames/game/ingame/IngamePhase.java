package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;
import net.twerion.hungergames.game.AbstractGamePhase;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;

public final class IngamePhase extends AbstractGamePhase  {
  @Inject
  private IngamePhase(
    @PhaseBound(IngamePhase.class) GamePhaseConfig config
  ) {
    super(config);
  }

}

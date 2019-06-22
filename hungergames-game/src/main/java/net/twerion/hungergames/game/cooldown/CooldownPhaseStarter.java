package net.twerion.hungergames.game.cooldown;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseStarter;
import net.twerion.hungergames.game.ingame.IngamePhase;

public class CooldownPhaseStarter implements GamePhaseStarter {
  private Provider<CooldownPhase> cooldownPhaseProvider;

  @Inject
  private CooldownPhaseStarter(
      Provider<CooldownPhase> cooldownPhaseProvider
  ) {
    this.cooldownPhaseProvider = cooldownPhaseProvider;
  }

  @Override
  public void start(Game game) {
    game.updatePhase(cooldownPhaseProvider.get());
  }

  @Override
  public boolean canStart(Game game) {
    return game.phase() instanceof IngamePhase;
  }
}

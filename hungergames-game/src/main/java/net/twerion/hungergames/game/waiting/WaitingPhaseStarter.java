package net.twerion.hungergames.game.waiting;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseStarter;

public final class WaitingPhaseStarter implements GamePhaseStarter {
  private Provider<WaitingPhase> factory;

  @Inject
  private WaitingPhaseStarter(Provider<WaitingPhase> factory) {
    this.factory = factory;
  }

  @Override
  public void start(Game game) {
    WaitingPhase phase = factory.get();
    game.beginInitialPhase(phase);
  }

  @Override
  public boolean canStart(Game game) {
    return false;
  }
}

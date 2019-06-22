package net.twerion.hungergames.game.ingame;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.twerion.hungergames.game.*;
import net.twerion.hungergames.game.warmup.WarmupPhase;
import net.twerion.hungergames.locale.MessageStore;
import net.twerion.hungergames.user.PlayerState;

public class IngamePhaseStarter implements GamePhaseStarter {
  private PlayerState ingamePlayerState;
  private Provider<IngamePhase> ingamePhaseProvider;

  @Inject
  private IngamePhaseStarter(
    Provider<IngamePhase> ingamePhaseProvider,
    @PhaseBound(IngamePhase.class) PlayerState ingamePlayerState
  ) {
    this.ingamePhaseProvider = ingamePhaseProvider;
    this.ingamePlayerState = ingamePlayerState;
  }

  @Override
  public void start(Game game) {
    game.tributes().forEach(ingamePlayerState::apply);
    game.updatePhase(ingamePhaseProvider.get());
  }

  @Override
  public boolean canStart(Game game) {
    return game.phase() instanceof WarmupPhase;
  }
}

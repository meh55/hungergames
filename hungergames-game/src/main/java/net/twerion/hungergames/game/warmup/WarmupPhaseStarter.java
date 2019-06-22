package net.twerion.hungergames.game.warmup;

import com.google.inject.Inject;
import com.google.inject.Provider;

import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseStarter;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.waiting.WaitingPhase;
import net.twerion.hungergames.user.PlayerState;
import org.bukkit.Bukkit;

public final class WarmupPhaseStarter implements GamePhaseStarter {
  private Provider<WarmupPhase> factory;
  private PlayerState warmupPlayerState;

  @Inject
  private WarmupPhaseStarter(
    Provider<WarmupPhase> factory,
    @PhaseBound(WarmupPhase.class) PlayerState warmupPlayerState
  ) {
    this.factory = factory;
    this.warmupPlayerState = warmupPlayerState;
  }

  @Override
  public void start(Game game) {
    game.initializeTributes(Bukkit.getOnlinePlayers());
    game.tributes().forEach(warmupPlayerState::apply);
    game.updatePhase(factory.get());
  }

  @Override
  public boolean canStart(Game game) {
    return game.phase() instanceof WaitingPhase;
  }
}

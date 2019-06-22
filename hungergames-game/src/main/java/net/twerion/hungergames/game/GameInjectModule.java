package net.twerion.hungergames.game;

import com.google.inject.AbstractModule;

import net.twerion.hungergames.Component;

@Component
public final class GameInjectModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Game.class).asEagerSingleton();
    bind(GamePhaseContext.class).asEagerSingleton();
    bind(PhaseBoundListenerGroupFactory.class).asEagerSingleton();
  }
}

package net.twerion.hungergames.loot;

import com.google.inject.AbstractModule;
import net.twerion.hungergames.Component;

@Component
public class LootInjectModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(LocationSource.class).to(ArenaSourceLootDropProvider.class);
    bind(TierProvider.class).to(ArenaSourceTierProvider.class);
    bind(Loot.class).toProvider(StaticLootFactory.class);
  }
}

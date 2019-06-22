package net.twerion.hungergames.game.waiting;

import com.google.inject.AbstractModule;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.twerion.hungergames.game.ArenaLoader;
import net.twerion.hungergames.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import net.twerion.hungergames.Component;
import net.twerion.hungergames.game.rule.CommonGameRule;
import net.twerion.hungergames.game.rule.CommonGameRuleSet;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.user.PlayerFlyPolicy;
import net.twerion.hungergames.user.PlayerState;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import javax.inject.Named;

@Component
public final class WaitingPhaseInjectModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @PhaseBound(WaitingPhase.class)
  GamePhaseConfig createConfig() {
    return GamePhaseConfig.newBuilder()
      .withRules(createWaitingPhaseRules())
      .withPlayerState(createWaitingPhasePlayerState())
      .create();
  }

  @Provides
  @Named("spawnPoint")
  Location getSpawnPoint() {
    return Bukkit.getWorld("world").getSpawnLocation();
  }

  @Inject
  @Provides
  Arena loadArena(Plugin plugin) {
    ArenaLoader arenaLoader = ArenaLoader.create();
    return arenaLoader.loadArena("arena", plugin.getDataFolder().toPath());
  }

  CommonGameRuleSet createWaitingPhaseRules() {
    return CommonGameRuleSet.allow(
      CommonGameRule.MOVE
    );
  }

  PlayerState createWaitingPhasePlayerState() {
    return PlayerState.newDefaultBuilder()
      .withGameMode(GameMode.ADVENTURE)
      .withFlyPolicy(PlayerFlyPolicy.DISALLOW_FLYING)
      .withOneHearth()
      .create();
  }
}

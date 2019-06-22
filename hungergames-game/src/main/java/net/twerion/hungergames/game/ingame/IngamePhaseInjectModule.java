package net.twerion.hungergames.game.ingame;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.arena.Arena;
import net.twerion.hungergames.border.Border;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.rule.CommonGameRule;
import net.twerion.hungergames.game.rule.CommonGameRuleSet;
import net.twerion.hungergames.loot.ArenaSourceLootDropProvider;
import net.twerion.hungergames.loot.ArenaSourceTierProvider;
import net.twerion.hungergames.loot.LocationSource;
import net.twerion.hungergames.loot.TierProvider;
import net.twerion.hungergames.user.PlayerFlyPolicy;
import net.twerion.hungergames.user.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import javax.inject.Named;

@Component
public final class IngamePhaseInjectModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  @PhaseBound(IngamePhase.class)
  GamePhaseConfig createConfig() {
    return GamePhaseConfig.newBuilder()
      .withRules(createIngamePhaseRules())
      .withPlayerState(createPlayerState())
      .create();
  }

  @Provides
  @PhaseBound(IngamePhase.class)
  PlayerState createPlayerState() {
    return PlayerState.newDefaultBuilder().create();
  }

  @Provides
  @PhaseBound(IngamePhase.class)
  CommonGameRuleSet createIngamePhaseRules() {
    return CommonGameRuleSet.allow(
      CommonGameRule.MOVE,
      CommonGameRule.ENTITY_DAMAGE,
      CommonGameRule.ITEM_PICKUP,
      CommonGameRule.ITEM_DROP,
      CommonGameRule.ENTITY_EXPLOSION,
      CommonGameRule.PHYSICAL_INTERACTION,
      CommonGameRule.HUNGER
    );
  }

  @Provides
  @Named("spectator")
  PlayerState createSpectatorState() {
    return PlayerState.newDefaultBuilder()
      .withFlyPolicy(PlayerFlyPolicy.ALLOW_FLYING)
      .withGameMode(GameMode.SPECTATOR)
      .withFullHealth()
      .withoutArmor()
      .withoutItems()
      .withFullFoodLevel()
      .create();
  }

  @Inject
  @Provides
  Border createBorder(Game game, Arena arena) {
    World world = Bukkit.getWorld(arena.worldName());
    return Border.newBuilder()
      .withGame(game)
      .withWorld(world)
      .withWorldBorder(world.getWorldBorder())
      .withInitialSize(400)
      .withMinSize(100)
      .withPlayerFactor(50)
      .withStep(0.5f)
      .withFrequency(5)
      .create();
  }
}

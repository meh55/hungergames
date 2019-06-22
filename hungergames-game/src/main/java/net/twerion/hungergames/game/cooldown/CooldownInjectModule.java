package net.twerion.hungergames.game.cooldown;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.countdown.Countdown;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.ingame.IngamePhaseStarter;
import net.twerion.hungergames.game.rule.CommonGameRule;
import net.twerion.hungergames.game.rule.CommonGameRuleSet;
import net.twerion.hungergames.game.warmup.WarmupPhase;
import net.twerion.hungergames.locale.MessageStore;
import net.twerion.hungergames.user.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;

@Component
public final class CooldownInjectModule extends AbstractModule {
  @Override
  protected void configure() {
  }

  @Provides
  @PhaseBound(CooldownPhase.class)
  GamePhaseConfig createConfig() {
    return GamePhaseConfig.newBuilder()
      .withRules(createCooldownPhaseRules())
      .withPlayerState(createCooldownPhasePlayerState())
      .create();
  }

  CommonGameRuleSet createCooldownPhaseRules() {
    return CommonGameRuleSet.allow(
      CommonGameRule.MOVE
    );
  }

  @Provides
  @PhaseBound(CooldownPhase.class)
  PlayerState createCooldownPhasePlayerState() {
    return PlayerState.newDefaultBuilder()
      .withGameMode(GameMode.SURVIVAL)
      .withFullFoodLevel()
      .withFullHealth()
      .create();
  }

  @Provides
  @PhaseBound(CooldownPhase.class)
  Countdown createCountdown(
    MessageStore messages
  ) {
    // TODO(merlinosayimwen): Remove hardcoding of times
    return Countdown.newBuilder()
      .withInitialCount(0, 15)
      .withTargetCount(0)
      .withDirection(Countdown.Direction.DECREMENTING)
      .withTickListener((minutes, seconds) -> {
        switch (seconds) {
          case 15:
          case 10:
          case 5:
          case 3:
          case 2:
            broadcastSound(Sound.NOTE_BASS_DRUM);
            Bukkit.broadcastMessage(messages.find("message.cooldown.countdown.seconds", seconds));
            break;
          case 1:
            broadcastSound(Sound.NOTE_BASS_DRUM);
            Bukkit.broadcastMessage(messages.find("message.cooldown.countdown.second"));
            break;
        }
      }).withCompletionListener(
        () -> {
          Bukkit.broadcastMessage(messages.find("message.cooldown.countdown.complete"));
          broadcastSound(Sound.NOTE_BASS_GUITAR);
          Bukkit.getServer().shutdown();
        }
      ).create();
  }

  private static void broadcastSound(Sound sound) {
    Bukkit.getOnlinePlayers().forEach(
      player -> player.playSound(player.getEyeLocation(), sound, 1, 1)
    );
  }
}

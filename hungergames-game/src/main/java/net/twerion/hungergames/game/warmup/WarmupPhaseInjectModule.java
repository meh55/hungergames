package net.twerion.hungergames.game.warmup;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import net.twerion.hungergames.Component;
import net.twerion.hungergames.countdown.Countdown;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhaseConfig;
import net.twerion.hungergames.game.PhaseBound;
import net.twerion.hungergames.game.ingame.IngamePhaseStarter;
import net.twerion.hungergames.game.rule.CommonGameRuleSet;
import net.twerion.hungergames.locale.MessageStore;
import net.twerion.hungergames.user.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;

@Component
public final class WarmupPhaseInjectModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  @PhaseBound(WarmupPhase.class)
  GamePhaseConfig createConfig() {
    return GamePhaseConfig.newBuilder()
      .withRules(createWarmupPhaseRules())
      .withPlayerState(createWarmupPhasePlayerState())
      .create();
  }

  CommonGameRuleSet createWarmupPhaseRules() {
    return CommonGameRuleSet.allow();
  }

  @Provides
  @PhaseBound(WarmupPhase.class)
  PlayerState createWarmupPhasePlayerState() {
    return PlayerState.newDefaultBuilder()
      .withGameMode(GameMode.SURVIVAL)
      .withFullFoodLevel()
      .withFullHealth()
      .create();
  }

  @Provides
  @PhaseBound(WarmupPhase.class)
  Countdown createCountdown(
    Game game,
    MessageStore messages,
    IngamePhaseStarter ingamePhaseStarter
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
            Bukkit.broadcastMessage(messages.find("message.warmup.countdown.seconds", seconds));
            break;
          case 1:
            broadcastSound(Sound.NOTE_BASS_DRUM);
            Bukkit.broadcastMessage(messages.find("message.warmup.countdown.second"));
            break;
        }
      }).withCompletionListener(
        () -> {
          Bukkit.broadcastMessage(messages.find("message.warmup.countdown.complete"));
          broadcastSound(Sound.NOTE_BASS_GUITAR);
          ingamePhaseStarter.start(game);
        }
      ).create();
  }

  private static void broadcastSound(Sound sound) {
    Bukkit.getOnlinePlayers().forEach(
      player -> player.playSound(player.getEyeLocation(), sound, 1, 1)
    );
  }
}

package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;

import net.twerion.hungergames.game.rule.CommonGameRuleSet;
import net.twerion.hungergames.user.PlayerState;

public class GamePhaseConfig {
  private CommonGameRuleSet rules;
  private PlayerState playerState;

  private GamePhaseConfig(CommonGameRuleSet rules, PlayerState playerState) {
    this.rules = rules;
    this.playerState = playerState;
  }

  public CommonGameRuleSet rules() {
    return rules;
  }

  public PlayerState playerState() {
    return playerState;
  }

  public static Builder newBuilder() {
    GamePhaseConfig prototype = new GamePhaseConfig(
      null, PlayerState.newDefaultBuilder().create()
    );
    return new Builder(prototype);
  }

  public static Builder newBuilder(GamePhaseConfig prototype) {
    return new Builder(new GamePhaseConfig(
      prototype.rules, prototype.playerState
    ));
  }

  public static final class Builder {
    private GamePhaseConfig prototype;

    private Builder(GamePhaseConfig prototype) {
      this.prototype = prototype;
    }

    public Builder withRules(CommonGameRuleSet rules) {
      Preconditions.checkNotNull(rules);
      prototype.rules = rules;
      return this;
    }

    public Builder withPlayerState(PlayerState state) {
      Preconditions.checkNotNull(state);
      prototype.playerState = state;
      return this;
    }

    public GamePhaseConfig create() {
      return new GamePhaseConfig(
        prototype.rules,
        prototype.playerState
      );
    }
  }
}

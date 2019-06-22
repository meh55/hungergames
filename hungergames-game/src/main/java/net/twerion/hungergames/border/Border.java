package net.twerion.hungergames.border;

import net.twerion.hungergames.Preconditions;
import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.ingame.IngamePhase;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Border {
  private WorldBorder border;
  private World world;
  private double lastBeginSize;
  private double targetSize;
  private double step;
  private double minSize;
  private int shrinkTime;
  private int frequency;
  private Game game;
  private int playerFactor;

  private Border() {
  }

  private Border(
    Game game,
    WorldBorder border,
    World world,
    double initialSize,
    double targetSize,
    double step,
    double minSize,
    int shrinkTime,
    int frequency,
    int playerFactor
  ) {
    this.game = game;
    this.border = border;
    this.world = world;
    this.targetSize = targetSize;
    this.step = step;
    this.minSize = minSize;
    this.shrinkTime = shrinkTime;
    this.frequency = frequency;
    this.playerFactor = playerFactor;
    this.lastBeginSize = initialSize;
  }

  public double targetSize() {
    return targetSize;
  }

  public WorldBorder worldBorder() {
    return border;
  }

  public void configure() {
    border.reset();
    border.setSize(lastBeginSize);
    border.setCenter(world.getSpawnLocation());
    border.setDamageAmount(10);
    border.setDamageBuffer(10);
    border.setWarningDistance(10);
    border.setWarningTime(10);
  }

  public void startShrinking(Game game, Plugin plugin) {
    startShrinking(game, plugin, nextSize(game));
  }

  public int nextSize(Game game) {
    return game.tributes().size() * playerFactor;
  }

  private boolean keepRunning(Game game) {
    return game.phase() instanceof IngamePhase || border.getSize() <= minSize;
  }

  void recomputeTarget() {
    targetSize = nextSize(game);
  }

  public void startShrinking(Game game, Plugin plugin, int targetSize) {
    this.targetSize = targetSize;
    BukkitRunnable task = new BukkitRunnable() {
      @Override
      public void run() {
        if (!update()) {
          if (!keepRunning(game)) {
            cancel();
          }
          recomputeTarget();
        }
      }
    };
    task.runTaskTimer(plugin, frequency, frequency);

  }

  public boolean update() {
    double nextSize = border.getSize() - step;
    if (nextSize <= targetSize) {
      border.setSize(targetSize);
      return true;
    }
    border.setSize(nextSize);
    return false;
  }

  public static Builder newBuilder() {
    return new Builder(new Border());
  }

  public static final class Builder {
    private Border prototype;

    private Builder(Border prototype) {
      this.prototype = prototype;
    }

    public Builder withGame(Game game) {
      Preconditions.checkNotNull(game);
      prototype.game = game;
      return this;
    }

    public Builder withWorldBorder(WorldBorder worldBorder) {
      Preconditions.checkNotNull(worldBorder);
      prototype.border = worldBorder;
      return this;
    }

    public Builder withWorld(World world) {
      Preconditions.checkNotNull(world);
      prototype.world = world;
      return this;
    }

    public Builder withInitialSize(int initialSize) {
      prototype.lastBeginSize = initialSize;
      return this;
    }

    public Builder withTargetSize(int targetSize) {
      prototype.targetSize = targetSize;
      return this;
    }

    public Builder withStep(float step) {
      prototype.step = step;
      return this;
    }

    public Builder withMinSize(int minSize) {
      prototype.minSize = minSize;
      return this;
    }

    public Builder withShrinkTime(int shrinkTime) {
      prototype.shrinkTime = shrinkTime;
      return this;
    }

    public Builder withPlayerFactor(int playerFactor) {
      prototype.playerFactor = playerFactor;
      return this;
    }

    public Builder withFrequency(int frequency) {
      prototype.frequency = frequency;
      return this;
    }

    public Border create() {
      return new Border(
        prototype.game,
        prototype.border,
        prototype.world,
        prototype.lastBeginSize,
        prototype.targetSize,
        prototype.step,
        prototype.minSize,
        prototype.shrinkTime,
        prototype.frequency,
        prototype.playerFactor
      );
    }
  }
}

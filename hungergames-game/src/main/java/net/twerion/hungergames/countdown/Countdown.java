package net.twerion.hungergames.countdown;

import net.twerion.hungergames.Preconditions;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BiConsumer;

public final class Countdown {
  public enum Direction {
    INCREMENTING,
    DECREMENTING
  }

  private int count;
  private int targetCount;
  private Direction direction;
  private BukkitRunnable task;
  private Runnable completionListener;
  private BiConsumer<Integer, Integer> tickListener;
  private boolean completed;

  private Countdown() {}

  private Countdown(
      Direction direction,
      int count,
      int targetCount,
      Runnable completionListener,
      BiConsumer<Integer, Integer> tickListener
  ) {
    this.direction = direction;
    this.count = count;
    this.targetCount = targetCount;
    this.completionListener = completionListener;
    this.tickListener = tickListener;
  }

  public void start(Plugin plugin) {
    this.task = new BukkitRunnable() {
      @Override
      public void run() {
        updateCount();
        if (completed) {
          cancel();
        }
      }
    };
    this.task.runTaskTimer(plugin, 20, 20);
  }

  public void tick() {
    tickListener.accept(minutes(), seconds());
  }

  public void complete() {
    completionListener.run();
    this.completed = true;
    // task.cancel();
  }

  public void updateCount() {
    int nextCount = direction == Direction.INCREMENTING
        ? count + 1
        : count - 1;
    tick();

    if (direction == Direction.INCREMENTING) {
      if (count >= targetCount) {
        complete();
      }
    } else {
      if (count <= targetCount) {
        complete();
      }
    }
    this.count = nextCount;
  }

  public int seconds() {
    return count % 60;
  }

  public int minutes() {
    return count / 60;
  }

  private String toTime() {
    return String.format("%02d:%02d", minutes(), seconds());
  }

  public static Builder newBuilder() {
    return new Builder(new Countdown());
  }

  public static final class Builder {
    private Countdown prototype;

    private Builder(Countdown prototype) {
      this.prototype = prototype;
    }

    public Builder withDirection(Direction direction) {
      Preconditions.checkNotNull(direction);
      prototype.direction = direction;
      return this;
    }

    public Builder withTargetCount(int minutes, int seconds) {
      return withTargetCount(minutes * 60 + seconds);
    }

    public Builder withTargetCount(int targetCount) {
      prototype.targetCount = targetCount;
      return this;
    }

    public Builder withInitialCount(int initialCount) {
      prototype.count = initialCount;
      return this;
    }

    public Builder withInitialCount(int minutes, int seconds) {
      return withInitialCount(minutes * 60 + seconds);
    }

    public Builder withCompletionListener(Runnable completionListener) {
      Preconditions.checkNotNull(completionListener);
      prototype.completionListener = completionListener;
      return this;
    }

    public Builder withTickListener(BiConsumer<Integer, Integer> listener) {
      Preconditions.checkNotNull(listener);
      prototype.tickListener = listener;
      return this;
    }

    public Countdown create() {
      return new Countdown(
        prototype.direction,
        prototype.count,
        prototype.targetCount,
        prototype.completionListener,
        prototype.tickListener
      );
    }
  }
}

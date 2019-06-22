package net.twerion.hungergames.user;

public final class Records {
  private int points;
  private int winCount;
  private int killCount;
  private int deathCount;
  private int roundCount;

  private Records() {}

  private Records(
      int points,
      int winCount,
      int killCount,
      int deathCount,
      int roundCount
  ) {
    this.points = points;
    this.winCount = winCount;
    this.killCount = killCount;
    this.deathCount = deathCount;
    this.roundCount = roundCount;
  }

  public int points() {
    return points;
  }

  public int winCount() {
    return winCount;
  }

  public int killCount() {
    return killCount;
  }

  public int deathCount() {
    return deathCount;
  }

  public int roundCount() {
    return roundCount;
  }

  public static Builder newBuilder() {
    return new Builder(new Records());
  }

  public static final class Builder {
    private Records prototype;

    private Builder(Records prototype) {
      this.prototype = prototype;
    }

    public Builder withPoints(int points) {
      prototype.points = points;
      return this;
    }

    public Builder withWinCount(int winCount) {
      prototype.winCount = winCount;
      return this;
    }

    public Builder withKillCount(int killCount) {
      prototype.killCount = killCount;
      return this;
    }

    public Builder withDeathCount(int deathCount) {
      prototype.deathCount = deathCount;
      return this;
    }

    public Builder withRoundCount(int roundCount) {
      prototype.roundCount = roundCount;
      return this;
    }

    public Records create() {
      return new Records(
        prototype.points,
        prototype.winCount,
        prototype.killCount,
        prototype.deathCount,
        prototype.roundCount
      );
    }
  }
}

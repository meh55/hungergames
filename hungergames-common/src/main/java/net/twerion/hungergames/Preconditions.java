package net.twerion.hungergames;

public final class Preconditions {
  private Preconditions() {}

  public static void checkNotNull(Object reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
  }
}

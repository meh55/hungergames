package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;

import java.lang.annotation.Annotation;

// Used by the injector
public final class PhaseBoundValue implements PhaseBound {
  private Class<? extends GamePhase> phase;

  private PhaseBoundValue(Class<? extends GamePhase> phase) {
    this.phase = phase;
  }

  @Override
  public Class<? extends GamePhase> value() {
    return phase;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return PhaseBound.class;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof PhaseBound)) {
      return false;
    }
    return isEqualTo((PhaseBound) other);
  }

  @Override
  public int hashCode() {
    return value().hashCode();
  }

  private boolean isEqualTo(PhaseBound bound) {
    return phase.equals(bound.value());
  }

  public static PhaseBoundValue of(Class<? extends GamePhase> phase) {
    Preconditions.checkNotNull(phase);
    return new PhaseBoundValue(phase);
  }
}

package net.twerion.hungergames.game;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import net.twerion.hungergames.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.twerion.hungergames.ComponentScanner;

public final class PhaseBoundListenerGroupFactory {
  private Injector injector;
  private ComponentScanner componentScanner;

  @Inject
  private PhaseBoundListenerGroupFactory(
      Injector injector,
      ComponentScanner componentScanner
  ) {
    this.injector = injector;
    this.componentScanner = componentScanner;
  }

  public PhaseBoundListenerGroup scanGroup(
      Class<? extends GamePhase> phaseType,
      Plugin plugin,
      Collection<Listener> explicit
  ) {
    Collection<Listener> listeners = componentScanner.classes()
      .findSubTypes(Listener.class)
      .filter(type -> isClassBoundToPhase(type, phaseType))
      .map(injector::getInstance)
      .collect(Collectors.toList());

    listeners.addAll(explicit);
    return PhaseBoundListenerGroup.create(phaseType, listeners, plugin);
  }

  private boolean isClassBoundToPhase(
      Class<?> type,
      Class<? extends GamePhase> phaseType
  ) {
    PhaseBound bind = type.getDeclaredAnnotation(PhaseBound.class);
    if (bind == null) {
      return false;
    }
    return phaseType.equals(bind.value());
  }

  public static PhaseBoundListenerGroupFactory create(
      Injector injector,
      ComponentScanner classPathScanner
  ) {
    Preconditions.checkNotNull(injector);
    Preconditions.checkNotNull(classPathScanner);
    return new PhaseBoundListenerGroupFactory(
      injector, classPathScanner
    );
  }
}

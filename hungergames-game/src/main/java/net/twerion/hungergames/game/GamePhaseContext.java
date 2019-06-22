package net.twerion.hungergames.game;

import net.twerion.hungergames.Preconditions;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import net.twerion.hungergames.ListenerGroup;

import java.util.Collection;
import java.util.Collections;

public final class GamePhaseContext {
  @Nullable private GamePhase current;
  @Nullable private ListenerGroup listeners;
  private Plugin plugin;
  private PluginManager eventChannel;
  private PhaseBoundListenerGroupFactory listenerGroupFactory;

  @Inject
  private GamePhaseContext(
      Plugin plugin,
      PluginManager eventChannel,
      PhaseBoundListenerGroupFactory listenerGroupFactory
  ) {
    this.plugin = plugin;
    this.eventChannel = eventChannel;
    this.listenerGroupFactory = listenerGroupFactory;
  }

  public void begin(GamePhase initial) {
    Preconditions.checkNotNull(initial);

    updatePhase(initial);
    beginCurrent();
  }

  public void change(GamePhase target) {
    Preconditions.checkNotNull(target);
    Preconditions.checkNotNull(current);

    endCurrent();
    updatePhase(target);
    beginCurrent();
  }

  public GamePhase current() {
    return current;
  }

  private void updatePhase(GamePhase target) {
    this.current = target;
    updateListenerGroup(target);
  }

  private void beginCurrent() {
    Preconditions.checkNotNull(listeners);
    Preconditions.checkNotNull(current);

    listeners.registerAll(eventChannel);
    current.begin();
    eventChannel.callEvent(GamePhaseBeginEvent.of(current));
  }

  private void endCurrent() {
    Preconditions.checkNotNull(current);
    Preconditions.checkNotNull(listeners);

    eventChannel.callEvent(GamePhaseEndEvent.of(current));
    listeners.unregisterAll(eventChannel);
    current.end();
  }

  private void updateListenerGroup(GamePhase targetPhase) {
    Collection<Listener> explicit = targetPhase instanceof Listener
      ? Collections.singleton((Listener)targetPhase)
      : Collections.emptyList();

    this.listeners = listenerGroupFactory.scanGroup(
      targetPhase.getClass(), plugin, explicit
    );
  }

}

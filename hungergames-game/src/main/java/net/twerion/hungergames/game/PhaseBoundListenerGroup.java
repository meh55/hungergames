package net.twerion.hungergames.game;

import java.util.Collection;
import java.util.logging.Logger;

import net.twerion.hungergames.Preconditions;

import net.twerion.hungergames.ListenerGroup;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class PhaseBoundListenerGroup implements ListenerGroup {
  private static final Logger LOG =
    Logger.getLogger(PhaseBoundListenerGroup.class.getSimpleName());

  private Class<? extends GamePhase> phase;
  private Collection<Listener> listeners;
  private Plugin plugin;

  private PhaseBoundListenerGroup(
      Class<? extends GamePhase> phase,
      Collection<Listener> listeners,
      Plugin plugin
  ) {
    this.phase = phase;
    this.listeners = listeners;
    this.plugin = plugin;
  }

  @Override
  public void registerAll(PluginManager registry) {
    listeners.forEach(listener -> registry.registerEvents(listener, plugin));
  }

  @Override
  public void unregisterAll(PluginManager registry) {
    listeners.forEach(HandlerList::unregisterAll);
  }

  public Class<? extends GamePhase> phaseType() {
    return phase;
  }

  public static PhaseBoundListenerGroup create(
      Class<? extends GamePhase> phase,
      Collection<Listener> listeners,
      Plugin plugin
  ) {
    Preconditions.checkNotNull(phase);
    Preconditions.checkNotNull(listeners);
    Preconditions.checkNotNull(plugin);
    return new PhaseBoundListenerGroup(phase, listeners, plugin);
  }
}

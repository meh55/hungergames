package net.twerion.hungergames;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.twerion.hungergames.game.PhaseBound;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public final class NonPhaseBoundListenerScanner {
  private ComponentScanner components;
  private PluginManager eventChannel;
  private Injector injector;
  private Plugin plugin;

  @Inject
  private NonPhaseBoundListenerScanner(
    ComponentScanner componentScanner,
    PluginManager eventChannel,
    Injector injector,
    Plugin plugin
  ) {
    this.components = componentScanner;
    this.eventChannel = eventChannel;
    this.injector = injector;
    this.plugin = plugin;
  }

  public void scanAndRegister() {
    components.classes()
      .findSubTypes(Listener.class)
      .filter(this::isNotPhaseBound)
      .map(injector::getInstance)
      .forEach(listener -> eventChannel.registerEvents(listener, plugin));
  }

  private boolean isNotPhaseBound(Class<?> type) {
    return type.getDeclaredAnnotation(PhaseBound.class) == null;
  }
}

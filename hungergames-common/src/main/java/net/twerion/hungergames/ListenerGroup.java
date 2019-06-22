package net.twerion.hungergames;

import org.bukkit.plugin.PluginManager;

public interface ListenerGroup {

  void registerAll(PluginManager registry);
  void unregisterAll(PluginManager registry);
}

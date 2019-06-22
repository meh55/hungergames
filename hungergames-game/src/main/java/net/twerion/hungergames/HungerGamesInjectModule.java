package net.twerion.hungergames;

import com.google.inject.AbstractModule;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.twerion.hungergames.locale.MessageStore;
import net.twerion.hungergames.locale.ResourceBundleMessageStore;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Locale;
import java.util.ResourceBundle;

public final class HungerGamesInjectModule extends AbstractModule {
  private Plugin plugin;
  private ComponentScanner componentScanner;

  private HungerGamesInjectModule(Plugin plugin, ComponentScanner componentScanner) {
    this.plugin = plugin;
    this.componentScanner = componentScanner;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(ComponentScanner.class).toInstance(componentScanner);
    bind(Server.class).toProvider(Bukkit::getServer);
    bind(PluginManager.class).toProvider(Bukkit::getPluginManager);
  }

  @Provides
  @Singleton
  ResourceBundle createResourceBundle() {
    return ResourceBundle.getBundle("messages", Locale.ENGLISH);
  }

  @Inject
  @Provides
  @Singleton
  MessageStore createMessageStore(ResourceBundle resourceBundle) {
    return ResourceBundleMessageStore.create(resourceBundle);
  }

  public static HungerGamesInjectModule create(
      Plugin plugin, ComponentScanner componentScanner
  ) {
    Preconditions.checkNotNull(plugin);
    Preconditions.checkNotNull(componentScanner);
    return new HungerGamesInjectModule(plugin, componentScanner);
  }
}
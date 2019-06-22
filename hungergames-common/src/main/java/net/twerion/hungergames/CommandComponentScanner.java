package net.twerion.hungergames;

import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

public final class CommandComponentScanner {
  private static final Logger LOG =
    Logger.getLogger(CommandComponentScanner.class.getSimpleName());

  private Injector injector;
  private Plugin plugin;
  private ComponentScanner componentScanner;

  @Inject
  private CommandComponentScanner(
    Injector injector, Plugin plugin, ComponentScanner componentScanner) {
    this.plugin = plugin;
    this.injector = injector;
    this.componentScanner = componentScanner;
  }

  public void scanAndRegister() {
    componentScanner.classes()
      .findSubTypes(CommandExecutor.class)
      .forEach(this::registerType);
  }

  private void registerType(Class<? extends CommandExecutor> type) {
    CommandLabel label = findCommandLabel(type);
    if (label == null) {
      LOG.warning(String.format("Invalid command component: %s does not " +
        "contain a @CommandLabel annotation. It is not registered", type.getName()));
      return;
    }
    CommandExecutor executor = injector.getInstance(type);
    register(executor, label, plugin);
  }

  private CommandLabel findCommandLabel(Class<? extends CommandExecutor> type) {
    return type.getDeclaredAnnotation(CommandLabel.class);
  }

  private void register(CommandExecutor executor, CommandLabel label, Plugin plugin) {
    PluginCommand command = findCommand(label.value(), plugin.getServer());
    if (command == null) {
      LOG.warning(String.format("Could not find command with label %s", label.value()));
      return;
    }
    command.setExecutor(executor);
    if (executor instanceof Restricted) {
      ((Restricted)executor).setPermission(command.getPermission());
    }
  }

  @Nullable
  private PluginCommand findCommand(String name, Server server) {
    return server.getPluginCommand(name.toLowerCase());
  }
}
package net.twerion.hungergames.user;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

import net.twerion.hungergames.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

@Component
public class UserInjectModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(ConfiguredTeamSet.class).toProvider(ConfiguredTeamSetReader.class);
  }

  @Inject
  @Provides
  TeamRepository createTeamRepository(
    ConfiguredTeamSet teamSet
  ) {
    return TeamRepository.create(teamSet);
  }

  @Inject
  @Provides
  FileConfiguration loadConfig(
    Plugin plugin
  ) {
    return plugin.getConfig();
  }
}

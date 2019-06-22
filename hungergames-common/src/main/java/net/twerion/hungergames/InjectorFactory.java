package net.twerion.hungergames;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public final class InjectorFactory {
  private static final Logger LOG =
    Logger.getLogger(InjectorFactory.class.getSimpleName());

  private ClassScanner classScanner;
  private Collection<Module> manuelModules;

  private InjectorFactory(
      ClassScanner classScanner,
      Collection<Module> manuelModules
  ) {
    this.classScanner = classScanner;
    this.manuelModules = manuelModules;
  }

  private Collection<Module> findModules() {
    return classScanner
      .findSubTypes(Module.class)
      .map(this::instantiateModule)
      .collect(Collectors.toList());
  }

  @Nullable
  private Module instantiateModule(Class<? extends Module> modules) {
    try {
      return modules.newInstance();
    } catch (IllegalAccessException | InstantiationException failedInstantiation) {
      LOG.warning("Could not instantiate injector module");
      LOG.warning("The plugin will still load");
      LOG.warning(failedInstantiation.getMessage());
      return null;
    }
  }

  public Injector createInjector() {
    Collection<Module> modules = findModules();
    modules.addAll(manuelModules);

    Consumer<Module> moduleLogger = module ->
      LOG.info(String.format("  - %s", module.getClass().getName()));

    LOG.info("Loaded the following Guice Modules:");
    modules.forEach(moduleLogger);
    return Guice.createInjector(modules);
  }

  public static InjectorFactory create(
      ClassScanner scanner, Collection<Module> manuelModules
  ) {
    Preconditions.checkNotNull(scanner);
    Preconditions.checkNotNull(manuelModules);
    return new InjectorFactory(scanner, manuelModules);
  }
}

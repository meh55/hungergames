package net.twerion.hungergames;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import com.google.inject.Injector;
import com.google.common.reflect.ClassPath;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.twerion.hungergames.game.Game;
import net.twerion.hungergames.game.GamePhase;
import net.twerion.hungergames.game.waiting.WaitingPhaseStarter;

/**
 * The HungerGame plugins entry point. Starts registration of listeners and
 * commands and creates the injector.
 * <p>
 * The plugins components to never access any of the fields of this class. They
 * may obtain an instance to it through dependency injection. This instance is
 * only used to use framework features such as the Bukkit task scheduling.
 *
 * @see HungerGamesInjectModule
 */
@SuppressWarnings("UnstableApiUsage")
public final class HungerGamesPlugin extends JavaPlugin {
  private static final Logger LOG = Logger.getLogger(HungerGamesPlugin.class.getSimpleName());

  private Injector injector;

  // The framework requires this constructor to be public.
  public HungerGamesPlugin() {
    this.injector = createInjector();
  }

  @Override
  public void onEnable() {
    registerComponents();
    startGame();
  }

  private void startGame() {
    Game game = injector.getInstance(Game.class);
    WaitingPhaseStarter waitingPhaseStarter =
      injector.getInstance(WaitingPhaseStarter.class);

    waitingPhaseStarter.start(game);
  }

  private void registerComponents() {
    CommandComponentScanner commandScanner =
      injector.getInstance(CommandComponentScanner.class);
    commandScanner.scanAndRegister();
    NonPhaseBoundListenerScanner listenerScanner =
      injector.getInstance(NonPhaseBoundListenerScanner.class);
    listenerScanner.scanAndRegister();
  }

  @Override
  public void onDisable() {
  }

  /**
   * Creates a ClassPath from the classes ClassLoader and shuts down the
   * server if the operation fails after logging the failure.
   *
   * @return ClassPath of the HungerGamesPlugin classes ClassLoader.
   */
  private ClassPath getClassPathOrDie() {
    try {
      return ClassPath.from(GamePhase.class.getClassLoader());
    } catch (IOException ioFailure) {
      LOG.severe("Could not create ClassPath: Killing Server.");
      LOG.severe(ioFailure.getMessage());
      Bukkit.getServer().shutdown();
      throw new IllegalStateException();
    }
  }

  /**
   * Creates a Guice Injector from all Modules annotated with @Component and
   * the core module (HungerGamesInjectModule).
   * <p>
   * This method scans the ClassPath for 'components' and creates a new
   * ComponentScanner instance that then is put into the dependency pool. This
   * allows future component lookups to be a lot faster. If the method fails
   * to create a classpath, the server  shuts down. This is done to prevent
   * undefined behaviours scenarios.
   *
   * @return Instance of the Guice Injector.
   */
  private Injector createInjector() {
    ClassScanner classScanner = ClassScanner.create(getClassPathOrDie());
    ComponentScanner componentScanner = ComponentScanner.of(classScanner);
    componentScanner.loadComponents();
    HungerGamesInjectModule coreInjectModule = HungerGamesInjectModule.create(
      this, componentScanner
    );
    InjectorFactory factory = InjectorFactory.create(
      componentScanner.classes(),
      Collections.singleton(coreInjectModule)
    );
    return factory.createInjector();
  }
}

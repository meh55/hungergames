package net.twerion.hungergames;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public final class ComponentScanner {
  private ClassScanner classScanner;
  private volatile Collection<Class<?>> componentClasses;

  private ComponentScanner(
      ClassScanner classScanner,
      Collection<Class<?>> componentClasses
  ) {
    this.classScanner = classScanner;
    this.componentClasses = componentClasses;
  }

  public void fuckThis() {
    System.out.println(Arrays.deepToString(componentClasses.toArray()));
  }

  public void loadComponents() {
    this.componentClasses = classScanner
      .findAnnotated(Component.class)
      .peek(type -> System.out.println(type.getName()))
      .collect(Collectors.toList());
  }

  public ClassScanner classes() {
    return ClassScanner.of(componentClasses);
  }

  public static ComponentScanner of(ClassScanner classScanner) {
    Preconditions.checkNotNull(classScanner);
    return new ComponentScanner(classScanner, new ArrayList<>());
  }
}

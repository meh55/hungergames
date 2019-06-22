package net.twerion.hungergames;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.reflect.ClassPath;

@SuppressWarnings("UnstableApiUsage")
public final class ClassScanner {
  private Collection<Class<?>> loadedClasses;

  private ClassScanner(Collection<Class<?>> loadedClasses) {
    this.loadedClasses = loadedClasses;
  }

  public Stream<Class<?>> findNonAnnotated(Annotation annotation) {
    return loadedClasses.stream()
      .filter(type -> !isAnnotated(type, annotation));
  }

  public Stream<Class<?>> findAnnotated(Annotation annotation) {
    return loadedClasses.stream()
      .filter(type -> isAnnotated(type, annotation));
  }

  private boolean isAnnotated(Class<?> type, Annotation annotation) {
    Annotation typesAnnotation =
      type.getDeclaredAnnotation(annotation.annotationType());

    if (typesAnnotation == null) {
      return false;
    }
    return annotation.equals(typesAnnotation);
  }

  public Stream<Class<?>> findNonAnnotated(Class<? extends Annotation> annotationType) {
    return loadedClasses.stream()
      .filter(type -> !isAnnotated(type, annotationType));
  }

  public Stream<Class<?>> findAnnotated(Class<? extends Annotation> annotationType) {
    return loadedClasses.stream()
      .filter(type -> isAnnotated(type, annotationType));
  }

  private boolean isAnnotated(
    Class<?> type, Class<? extends Annotation> annotationType) {
    Annotation typesAnnotation = type.getDeclaredAnnotation(annotationType);
    return typesAnnotation != null;
  }

  public <E> Stream<Class<E>> findSubTypes(Class<E> superType) {
    return loadedClasses.stream()
      .filter(type -> isSubType(type, superType))
      .filter(this::isNoInterface)
      .map(this::unsafeTypeCast);
  }

  private boolean isSubType(Class<?> type, Class<?> target) {
    return target.isAssignableFrom(type);
  }

  private boolean isNoInterface(Class<?> type) {
    return !type.isInterface();
  }

  @SuppressWarnings("unchecked")
  private <E> Class<E> unsafeTypeCast(Class<?> type) {
    return (Class<E>) type;
  }


  public static ClassScanner create(ClassPath path) {
    return create(path.getAllClasses());
  }

  public static ClassScanner createInPackage(
    ClassPath path, String packageName) {

    return create(path.getTopLevelClasses(packageName));
  }

  public static ClassScanner of(Collection<Class<?>> classes) {
    Preconditions.checkNotNull(classes);
    return new ClassScanner(classes);
  }

  private static ClassScanner create(Collection<ClassPath.ClassInfo> classes) {
    Collection<Class<?>> loaded = new ArrayList<>();
    for (ClassPath.ClassInfo info : classes) {
      try {
        loaded.add(info.load());
      } catch (NoClassDefFoundError ignored) {
        // This exception is thrown a lot and almost all of the time, we
        // do not really care about it. All the classes from our plugin
        // are loaded anyways.
      }
    }
    return new ClassScanner(loaded);
  }
}


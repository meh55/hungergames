package net.twerion.hungergames.locale;

import java.text.MessageFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.twerion.hungergames.Preconditions;

import javax.annotation.Nullable;

public final class ResourceBundleMessageStore implements MessageStore {
  private static final Logger LOG =
    Logger.getLogger(ResourceBundleMessageStore.class.getSimpleName());

  private static final String PREFIX_FIELD = "common.prefix";
  private static final String PREFIXED_MESSAGE_PREFIX = "message.";

  private String fallback;
  private String prefix;
  private Map<String, String> prefixed;
  private ResourceBundle resources;

  private ResourceBundleMessageStore(
    String fallback,
    String prefix,
    Map<String, String> prefixed,
    ResourceBundle resources
  ) {
    this.prefixed = prefixed;
    this.prefix = prefix;
    this.fallback = fallback;
    this.resources = resources;
  }

  @Nullable
  private String lookup(String key) {
    return prefixed.computeIfAbsent(key, this::lookupStringInResourceSet);
  }

  @Nullable
  private String lookupStringInResourceSet(String key) {
    try {
      String rawMessage = translateMessage(resources.getString(key));
      if (key.startsWith(PREFIXED_MESSAGE_PREFIX)) {
        return prefix.concat(rawMessage);
      }
      return rawMessage;
    } catch (MissingResourceException resourceIsMissing) {
      return null;
    }
  }

  @Override
  public String find(MessageKey key) {
    String message = lookup(key.key());
    if (message != null) {
      return message;
    }
    reportMissingResources(key.key());
    return key.fallback().orElse(fallback);
  }

  @Override
  public String find(String key) {
    String message = lookup(key);
    if (message != null) {
      return message;
    }
    reportMissingResources(key);
    return fallback;
  }

  @Override
  public String find(String key, Object... arguments) {
    String message = lookup(key);
    if (message != null) {
      return MessageFormat.format(message, arguments);
    }
    reportMissingResources(key);
    return fallback;
  }

  @Override
  public String find(MessageKey key, Object... arguments) {
    String message = lookup(key.key());
    if (message != null) {
      return MessageFormat.format(message, arguments);
    }
    reportMissingResources(key.key());
    return fallback;
  }

  @Override
  public Optional<String> findOptional(MessageKey key) {
    return findOptional(key.key());
  }

  @Override
  public Optional<String> findOptional(String key) {
    return Optional.of(lookup(key));
  }

  private void reportMissingResources(String key) {
    LOG.warning(
      String.format("The bundle %s does not contain key '%s'", resources.getBaseBundleName(), key));
  }

  private static String translateMessage(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static ResourceBundleMessageStore create(
    ResourceBundle resources) {

    return create(resources, "message is missing");
  }

  private static String readPrefix(ResourceBundle bundle) {
    try {
      return bundle.getString(PREFIX_FIELD);
    } catch (MissingResourceException noSuchField) {
      LOG.warning("No 'prefix' field found in ResourceBundle");
      return "";
    }
  }

  public static ResourceBundleMessageStore create(
      ResourceBundle resources, String fallback
  ) {
    Preconditions.checkNotNull(resources);
    Preconditions.checkNotNull(fallback);

    String prefix = readPrefix(resources);
    return create(resources, fallback, translateMessage(prefix));
  }

  public static ResourceBundleMessageStore create(
      ResourceBundle resources, String fallback, String prefix
  ) {
    Preconditions.checkNotNull(resources);
    Preconditions.checkNotNull(fallback);
    Preconditions.checkNotNull(prefix);

    return new ResourceBundleMessageStore(
      fallback, prefix, new HashMap<>(), resources
    );
  }
}

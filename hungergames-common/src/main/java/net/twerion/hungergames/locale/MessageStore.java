package net.twerion.hungergames.locale;

import java.util.Optional;

public interface MessageStore {

  String find(MessageKey key);

  String find(String key);

  String find(MessageKey key, Object... arguments);

  String find(String key, Object... arguments);

  Optional<String> findOptional(MessageKey key);

  Optional<String> findOptional(String key);
}

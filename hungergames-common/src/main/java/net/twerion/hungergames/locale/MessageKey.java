package net.twerion.hungergames.locale;

import java.util.Optional;

public interface MessageKey {

  String key();

  Optional<String> fallback();
}

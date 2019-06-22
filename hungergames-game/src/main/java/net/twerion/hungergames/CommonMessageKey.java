package net.twerion.hungergames;

import net.twerion.hungergames.locale.MessageKey;

import javax.annotation.Nullable;
import java.util.Optional;

public enum CommonMessageKey implements MessageKey {
  RESTRICTED_ACTION("message.restricted", "Action is not permitted");

  @Nullable
  private String fallback;
  private String key;

  CommonMessageKey(String key) {
    this.key = key;
  }

  CommonMessageKey(String key, String fallback) {
    this.key = key;
    this.fallback = fallback;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Optional<String> fallback() {
    return Optional.ofNullable(fallback);
  }
}

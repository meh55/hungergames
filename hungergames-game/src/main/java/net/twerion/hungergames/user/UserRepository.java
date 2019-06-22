package net.twerion.hungergames.user;

import com.google.inject.Inject;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class UserRepository {
  private Map<UUID, User> store;
  private UserFactory userFactory;

  @Inject
  private UserRepository(UserFactory userFactory) {
    this(new HashMap<>(), userFactory);
  }

  private UserRepository(Map<UUID, User> store, UserFactory userFactory) {
    this.store = store;
    this.userFactory = userFactory;
  }

  public User getInstance(Player player) {
    return store.computeIfAbsent(player.getUniqueId(),
      key -> userFactory.getInstance(player));
  }

  public Optional<User> getExistingInstance(Player player) {
    return Optional.ofNullable(store.get(player.getUniqueId()));
  }
}
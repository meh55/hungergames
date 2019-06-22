package net.twerion.hungergames.user;

import com.google.common.collect.ImmutableList;
import net.twerion.hungergames.Preconditions;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public final class Team {
  private String name;
  private Collection<User> users;

  private Team(String name, Collection<User> users) {
    this.name = name;
    this.users = users;
  }

  public String name() {
    return name;
  }

  public void win() {

  }

  void addMember(User user) {
    users.add(user);
  }

  public boolean contains(User target) {
    return users.contains(target);
  }

  public boolean contains(UUID uuid) {
    return users.stream().anyMatch(user -> user.player().getUniqueId().equals(uuid));
  }

  public boolean isEliminated() {
    return aliveCount() == 0;
  }

  private int aliveCount() {
    return (int) users.stream()
      .filter(User::isAlive)
      .count();
  }


  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Team)) {
      return false;
    }
    return name.equals(((Team) other).name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  public static Team of(String name, Collection<User> users) {
    Preconditions.checkNotNull(users);
    return new Team(name, ImmutableList.copyOf(users));
  }

  public static Team ofMutable(String name, Collection<User> users) {
    Preconditions.checkNotNull(users);
    return new Team(name, users);
  }
}

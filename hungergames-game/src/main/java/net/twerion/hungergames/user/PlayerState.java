package net.twerion.hungergames.user;

import javax.annotation.Nullable;

import net.twerion.hungergames.Preconditions;

import com.google.common.collect.ImmutableList;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public final class PlayerState {
  private static final int MAX_HEALTH = 20;
  private static final int MAX_FOOD_LEVEL = 20;

  private static final class Lazy {
    static final PlayerState DEFAULT = createDefaultState();
  }

  private int health;
  private int maxHealth;
  private int foodLevel;
  private int experience;
  private Collection<PlayerPolicy> policies;
  @Nullable private ItemStack[] inventoryContents;
  @Nullable private ItemStack[] armorContents;
  private GameMode gameMode;

  private PlayerState() {}

  private PlayerState(
      int health,
      int maxHealth,
      int foodLevel,
      int experience,
      Collection<PlayerPolicy> policies,
      ItemStack[] inventoryContents,
      ItemStack[] armorContents,
      GameMode gameMode
  ) {
    this.health = health;
    this.maxHealth = maxHealth;
    this.foodLevel = foodLevel;
    this.experience = experience;
    this.policies = policies;
    this.inventoryContents = inventoryContents;
    this.armorContents = armorContents;
    this.gameMode = gameMode;
  }

  public void apply(Player target) {
    applyPolicies(target);
    applyInventory(target);
    applyHealthSafely(target);
    target.setFoodLevel(foodLevel);
    target.setGameMode(gameMode);
    target.setTotalExperience(experience);
  }

  public void applyInventory(Player target) {
    target.getInventory().setArmorContents(armorContents);
    if (inventoryContents == null) {
      target.getInventory().clear();
    } else {
      target.getInventory().setContents(inventoryContents);
    }
  }

  public void applyPolicies(Player target) {
    policies.forEach(policy -> policy.apply(target));
  }

  public void applyHealthSafely(Player target) {
    if (target.getHealth() > maxHealth) {
      target.setHealth(health);
      target.setMaxHealth(maxHealth);
      return;
    }
    target.setMaxHealth(maxHealth);
    target.setHealth(health);
  }

  public static Builder newEmptyBuilder() {
    return new Builder(new PlayerState())
      .withPolicies(new ArrayList<>());
  }

  public static Builder newDefaultBuilder() {
    return newBuilder(Lazy.DEFAULT);
  }

  public static Builder newBuilder(PlayerState prototype) {
    Preconditions.checkNotNull(prototype);
    return newEmptyBuilder()
      .withHealth(prototype.health)
      .withMaxHealth(prototype.maxHealth)
      .withFoodLevel(prototype.foodLevel)
      .withExperience(prototype.experience)
      .withPolicies(new ArrayList<>(prototype.policies))
      .withInventoryContents(prototype.inventoryContents)
      .withArmorContents(prototype.armorContents)
      .withGameMode(prototype.gameMode);
  }

  public static PlayerState createDefaultState() {
    return newEmptyBuilder()
      .withFullHealth()
      .withFullFoodLevel()
      .withGameMode(GameMode.SURVIVAL)
      .withoutArmor()
      .withoutItems()
      .create();
  }

  public static final class Builder {
    private PlayerState prototype;

    private Builder(PlayerState prototype) {
      this.prototype = prototype;
    }

    public Builder withHealth(int health) {
      if (health > prototype.maxHealth) {
        prototype.maxHealth = health;
      }
      prototype.health = health;
      return this;
    }

    public Builder withMaxHealth(int maxHealth) {
      if (prototype.health > maxHealth) {
        prototype.health = maxHealth;
      }
      prototype.maxHealth = maxHealth;
      return this;
    }

    public Builder withFoodLevel(int foodLevel) {
      prototype.foodLevel = foodLevel;
      return this;
    }

    public Builder withFullFoodLevel() {
      return withFoodLevel(MAX_FOOD_LEVEL);
    }

    public Builder withOneHearth() {
      return withHealth(2)
        .withMaxHealth(2);
    }

    public Builder withFullHealth() {
      return withHealth(MAX_HEALTH);
    }

    public Builder withoutItems() {
      return withInventoryContents(null);
    }

    public Builder addPolicy(PlayerPolicy policy) {
      Preconditions.checkNotNull(policy);
      prototype.policies.add(policy);
      return this;
    }

    private void removeFlyPolicy(PlayerFlyPolicy policy) {
      prototype.policies.remove(policy);
    }

    public Builder withFlyPolicy(PlayerFlyPolicy policy) {
      Preconditions.checkNotNull(policy);
      switch (policy) {
        case ALLOW_FLYING: removeFlyPolicy(PlayerFlyPolicy.DISALLOW_FLYING);
        case DISALLOW_FLYING: removeFlyPolicy(PlayerFlyPolicy.ALLOW_FLYING);
      }
      return addPolicy(policy);
    }

    public Builder withInventoryContents(@Nullable ItemStack[] contents) {
      prototype.inventoryContents = contents == null ? null : contents.clone();
      return this;
    }

    public Builder withoutArmor() {
      prototype.armorContents = null;
      return this;
    }

    public Builder withArmorContents(@Nullable ItemStack[] contents) {
      if (contents == null) {
        prototype.armorContents = null;
      } else {
        prototype.armorContents = contents.clone();
      }
      return this;
    }

    public Builder withPolicies(Collection<PlayerPolicy> policies) {
      Preconditions.checkNotNull(policies);
      prototype.policies = new ArrayList<>(policies);
      return this;
    }

    public Builder withExperience(int experience) {
      prototype.experience = experience;
      return this;
    }

    public Builder withGameMode(GameMode gameMode) {
      Preconditions.checkNotNull(gameMode);
      prototype.gameMode = gameMode;
      return this;
    }

    public PlayerState create() {
      ItemStack[] inventoryContents = prototype.inventoryContents == null
        ? null : prototype.inventoryContents.clone();

      ItemStack[] armorContents = prototype.armorContents == null
        ? null : prototype.armorContents.clone();

      return new PlayerState(
        prototype.health,
        prototype.maxHealth,
        prototype.foodLevel,
        prototype.experience,
        ImmutableList.copyOf(prototype.policies),
        inventoryContents,
        armorContents,
        prototype.gameMode
      );
    }
  }
}

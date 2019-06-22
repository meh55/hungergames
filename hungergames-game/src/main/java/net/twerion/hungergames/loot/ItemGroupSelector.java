package net.twerion.hungergames.loot;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import net.twerion.hungergames.Preconditions;

public class ItemGroupSelector {
  private ItemGroup[] groups;
  private Random random;
  private int totalWeight;

  private ItemGroupSelector(ItemGroup[] groups, Random random, int totalWeight) {
    this.groups = groups;
    this.random = random;
    this.totalWeight = totalWeight;
  }

  public Optional<ItemStack> pickRandom(float luck) {
    int weight = random.nextInt(totalWeight);
    for (ItemGroup group : groups) {
      weight -= group.weight() * luck;
      if (weight <= 0) {
        return Optional.of(
          group.selectRandom(random.nextInt())
        );
      }
    }
    return Optional.empty();
  }

  public static ItemGroupSelector of(ItemGroup[] groups) {
    Preconditions.checkNotNull(groups);
    ItemGroup[] sorted = groups.clone();
    Arrays.sort(sorted);
    int weight = 0;
    for (ItemGroup group : sorted) {
      weight += group.weight();
    }
    return new ItemGroupSelector(sorted, new Random(), weight);
  }
}

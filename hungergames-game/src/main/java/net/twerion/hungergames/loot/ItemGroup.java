package net.twerion.hungergames.loot;

import net.twerion.hungergames.Preconditions;
import org.bukkit.inventory.ItemStack;

public class ItemGroup implements Comparable<ItemGroup> {
  private ItemStack[] items;
  private int weight;

  private ItemGroup(ItemStack[] items, int weight) {
    this.items = items;
    this.weight = weight;
  }

  public int weight() {
    return weight;
  }

  public ItemStack selectRandom(int seed) {
    return items[Math.abs(seed % items.length)];
  }

  @Override
  public int compareTo(ItemGroup itemGroup) {
    return Integer.compare(weight, itemGroup.weight);
  }

  public static ItemGroup of(ItemStack[] items, int weight) {
    Preconditions.checkNotNull(items);
    return new ItemGroup(items.clone(), weight);
  }
}

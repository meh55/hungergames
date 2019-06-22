package net.twerion.hungergames.loot;

import com.google.common.collect.Range;
import net.twerion.hungergames.Preconditions;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.ThreadLocalRandom;

public class Loot {
  private Range<Integer> itemCountRange;
  private ItemGroupSelector selector;

  private Loot(
    Range<Integer> itemCountRange,
    ItemGroupSelector selector
  ) {
    this.itemCountRange = itemCountRange;
    this.selector = selector;
  }

  public void fillInventory(float weight, Inventory inventory) {
    int itemCount = randomItemCount();
    int inventorySize = inventory.getSize();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int index = 0; index < inventorySize; index++) {
      if (random.nextInt(inventorySize) >= itemCount) {
        continue;
      }
      final int indexValue = index;
      selector.pickRandom(weight).ifPresent(
        item -> inventory.setItem(indexValue, item)
      );
    }
  }

  private int randomItemCount() {
    return ThreadLocalRandom.current().nextInt(
      itemCountRange.upperEndpoint() - itemCountRange.lowerEndpoint()
    ) + itemCountRange.lowerEndpoint();
  }

  public static Loot create(
    Range<Integer> itemCountRange, ItemGroupSelector selector) {

    Preconditions.checkNotNull(itemCountRange);
    Preconditions.checkNotNull(selector);
    return new Loot(itemCountRange, selector);
  }
}

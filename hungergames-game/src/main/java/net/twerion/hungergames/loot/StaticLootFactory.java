package net.twerion.hungergames.loot;

import com.google.common.collect.Range;
import com.google.inject.Provider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;


public class StaticLootFactory implements Provider<Loot> {

  @Override
  public Loot get() {
    return Loot.create(Range.closed(5, 10), createSelector());
  }

  private ItemGroupSelector createSelector() {
    return ItemGroupSelector.of(
      createGroups()
    );
  }

  private ItemGroup[] createGroups() {
    return new ItemGroup[]{
      ItemGroup.of(
        new ItemStack[]{
          item(DIAMOND),
          item(IRON_SWORD),
          item(IRON_CHESTPLATE),
          item(IRON_LEGGINGS),
          item(IRON_BOOTS),
          item(IRON_HELMET),
          item(GOLDEN_APPLE),
        },
        3
      ),
      ItemGroup.of(
        new ItemStack[]{
          item(GOLDEN_CARROT),
          item(DIAMOND),
          item(IRON_SWORD),
          item(IRON_CHESTPLATE),
          item(IRON_LEGGINGS),
          item(IRON_BOOTS),
          item(IRON_HELMET),
          item(GOLD_BOOTS),
          item(GOLD_CHESTPLATE),
          item(GOLD_LEGGINGS),
          item(GOLD_HELMET),
          item(GOLDEN_APPLE),
          item(DIAMOND_AXE),
          item(STONE_SWORD),
          item(ARROW, 8),
          item(BOW),
          item(FISHING_ROD),
          item(CHAINMAIL_BOOTS),
          item(CHAINMAIL_CHESTPLATE),
          item(CHAINMAIL_LEGGINGS),
          item(CHAINMAIL_HELMET),
        },
        10
      ),
      ItemGroup.of(
        new ItemStack[]{
          item(GRILLED_PORK, 2),
          item(PORK, 3),
          item(COOKED_FISH, 2),
          item(RAW_FISH, 3),
          item(COOKED_CHICKEN, 2),
          item(RAW_CHICKEN, 1),
          item(ARROW, 3),
          item(ARROW, 2),
          item(ARROW, 5),
          item(BOW),
          item(FEATHER),
          item(FISHING_ROD),
          item(STRING),
          item(STRING, 2),
          item(WOOD_SWORD),
          item(WOOD_AXE),
          item(GOLD_SWORD),
          item(BONE),
          item(WHEAT),
          item(TNT),
          item(EXP_BOTTLE),
          item(STICK),
          item(STONE_SWORD),
          item(LEATHER_BOOTS),
          item(LEATHER_CHESTPLATE),
          item(LEATHER_HELMET),
          item(LEATHER_LEGGINGS),
          item(GOLD_BOOTS),
          item(GOLD_CHESTPLATE),
          item(GOLD_LEGGINGS),
          item(GOLD_HELMET),
          item(CHAINMAIL_BOOTS),
          item(CHAINMAIL_CHESTPLATE),
          item(CHAINMAIL_LEGGINGS),
          item(CHAINMAIL_HELMET),
        },
        80
      ),

    };
  }

  private ItemStack item(Material material) {
    return new ItemStack(material);
  }

  private ItemStack item(Material material, int amount) {
    return new ItemStack(material, amount);
  }
}

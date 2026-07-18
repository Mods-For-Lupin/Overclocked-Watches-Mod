package io.github.jason13official.overclocked_watches.api.common.data;

import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModDataComponents;
import net.minecraft.world.item.ItemStack;

public class WatchItemData {

  public static ItemStack initializeCharges(WatchItem watch, ItemStack itemStack) {

    if (itemStack.has(ModDataComponents.CHARGES)) {
      return itemStack;
    }

    itemStack.set(ModDataComponents.CHARGES, watch.getTier().getWatchCharges());

    return itemStack.copy();
  }
}

package io.github.jason13official.overclocked_watches.api.common.data;

import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class WatchItemData {

  public static ItemStack initializeTag(WatchItem watch, ItemStack itemStack) {

    CompoundTag stackData = itemStack.getOrCreateTag();
    if (stackData.contains(WatchItem.CHARGES_TAG, CompoundTag.TAG_INT)) {
      return itemStack;
    }

    stackData.putInt(WatchItem.CHARGES_TAG, watch.getTier().getWatchCharges());
    itemStack.save(stackData);

    return itemStack.copy();
  }
}

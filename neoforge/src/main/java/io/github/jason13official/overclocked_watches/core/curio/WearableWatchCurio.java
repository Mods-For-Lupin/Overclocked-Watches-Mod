package io.github.jason13official.overclocked_watches.core.curio;

import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class WearableWatchCurio implements ICurio {

  private final WatchItem item;
  private final ItemStack stack;

  public WearableWatchCurio(WatchItem item, ItemStack stack) {
    this.item = item;
    this.stack = stack;
  }

  public WatchItem getItem() {
    return item;
  }

  @Override
  public ItemStack getStack() {
    return stack;
  }
}

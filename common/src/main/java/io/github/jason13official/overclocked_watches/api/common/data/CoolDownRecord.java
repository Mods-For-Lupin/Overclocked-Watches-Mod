package io.github.jason13official.overclocked_watches.api.common.data;

import net.minecraft.world.item.Item;

public record CoolDownRecord(Item item, int remain, int total) {

  public Item item() {
    return this.item;
  }

  public int remain() {
    return this.remain;
  }

  public int total() {
    return this.total;
  }
}

package io.github.jason13official.overclocked_watches.core.util;

import net.minecraft.world.item.Item;

public record CoolDownRecord(Item item, int remain, int total) {
    public CoolDownRecord(Item item, int remain, int total) {
        this.item = item;
        this.remain = remain;
        this.total = total;
    }

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

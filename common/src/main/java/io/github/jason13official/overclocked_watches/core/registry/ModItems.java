package io.github.jason13official.overclocked_watches.core.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.core.world.item.WatchItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class ModItems {

    public static final Item GOLDEN_WATCH = new WatchItem(WatchItem.Tier.GOLDEN);
    public static final Item DIAMOND_WATCH = new WatchItem(WatchItem.Tier.DIAMOND);
    public static final Item NETHERITE_WATCH = new WatchItem(WatchItem.Tier.NETHERITE);

    public static void register(BiConsumer<Item, ResourceLocation> consumer) {
        consumer.accept(GOLDEN_WATCH, OverclockedWatches.identifier("golden_watch"));
        consumer.accept(DIAMOND_WATCH, OverclockedWatches.identifier("diamond_watch"));
        consumer.accept(NETHERITE_WATCH, OverclockedWatches.identifier("netherite_watch"));
    }
}

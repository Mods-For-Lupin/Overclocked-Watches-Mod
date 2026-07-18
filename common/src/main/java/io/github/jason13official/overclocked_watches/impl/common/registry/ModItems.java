package io.github.jason13official.overclocked_watches.impl.common.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

  public static Item GOLDEN_WATCH; // = new WatchItem(WatchItem.Tier.GOLDEN);
  public static Item DIAMOND_WATCH; // = new WatchItem(WatchItem.Tier.DIAMOND);
  public static Item NETHERITE_WATCH; // = new WatchItem(WatchItem.Tier.NETHERITE);

  public static void register(BiConsumer<Item, ResourceLocation> consumer) {

    GOLDEN_WATCH = new WatchItem(WatchTier.GOLDEN);
    DIAMOND_WATCH = new WatchItem(WatchTier.DIAMOND);
    NETHERITE_WATCH = new WatchItem(WatchTier.NETHERITE);

    consumer.accept(GOLDEN_WATCH, OverclockedWatches.identifier("golden_watch"));
    consumer.accept(DIAMOND_WATCH, OverclockedWatches.identifier("diamond_watch"));
    consumer.accept(NETHERITE_WATCH, OverclockedWatches.identifier("netherite_watch"));
  }

  public static Item getWatch(WatchTier tier) {

    return switch (tier) {
      case GOLDEN -> GOLDEN_WATCH;
      case DIAMOND -> DIAMOND_WATCH;
      case NETHERITE -> NETHERITE_WATCH;
    };
  }
}

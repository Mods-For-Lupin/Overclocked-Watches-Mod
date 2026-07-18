package io.github.jason13official.overclocked_watches.impl.common.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModTabs {

  public static final CreativeModeTab OVERCLOCKED_WATCHES = Services.PLATFORM.tabBuilder()
      .icon(() -> new ItemStack(ModItems.DIAMOND_WATCH))
      .title(Component.translatable("itemGroup.overclockedWatches")).displayItems((itemDisplayParameters, output) -> {
        output.accept(ModItems.GOLDEN_WATCH);
        output.accept(ModItems.DIAMOND_WATCH);
        output.accept(ModItems.NETHERITE_WATCH);
      }).build();

  public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
    consumer.accept(OVERCLOCKED_WATCHES, OverclockedWatches.identifier("ow_tab"));
  }
}

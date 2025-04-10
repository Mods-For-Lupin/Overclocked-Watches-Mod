package com.cursee.overclocked_watches.core.registry;

import com.cursee.overclocked_watches.OverclockedWatches;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class ModTabs {

    public static final CreativeModeTab OW_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ModItems.DIAMOND_WATCH))
            .title(Component.translatable("itemGroup.overclockedWatches"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.GOLDEN_WATCH);
                output.accept(ModItems.DIAMOND_WATCH);
                output.accept(ModItems.NETHERITE_WATCH);
            }).build();

    public static void register(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        consumer.accept(OW_TAB, OverclockedWatches.identifier("ow_tab"));
    }
}

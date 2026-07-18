package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class OverclockedWatchesFabric implements ModInitializer {

  @Override
  public void onInitialize() {

    OverclockedWatches.init();

    bind(BuiltInRegistries.ITEM, ModItems::register);
  }

  public <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {

    source.accept((t, rl) -> Registry.register(registry, rl, t));
  }
}

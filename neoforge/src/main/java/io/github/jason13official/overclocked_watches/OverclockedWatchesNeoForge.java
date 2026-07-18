package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesNeoForge {

  public static IEventBus EVENT_BUS;

  public OverclockedWatchesNeoForge(IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    OverclockedWatches.init();

    bind(Registries.ITEM, ModItems::register);
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }
}
package io.github.jason13official.overclocked_watches.client.entity.renderer;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.impl.client.item.model.ArmsModel;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TrinketRenderers implements SimpleSynchronousResourceReloadListener {

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    Services.PLATFORM.registerWatchRenderer(ModItems.GOLDEN_WATCH, () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
    Services.PLATFORM.registerWatchRenderer(ModItems.DIAMOND_WATCH, () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
    Services.PLATFORM.registerWatchRenderer(ModItems.NETHERITE_WATCH, () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
  }

  @Override
  public ResourceLocation getFabricId() {
    return OverclockedWatches.identifier(Constants.MOD_ID + "_renderers");
  }
}

package io.github.jason13official.overclocked_watches.client.entity.renderer;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class TrinketRenderers implements SimpleSynchronousResourceReloadListener {

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    WatchRenderer.registerAll();
  }

  @Override
  public ResourceLocation getFabricId() {
    return OverclockedWatches.identifier(Constants.MOD_ID + "_renderers");
  }
}

package io.github.jason13official.overclocked_watches.impl.client.item;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import java.util.EnumMap;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

public class RendererLayers {

  private static final EnumMap<WatchTier, ModelLayerLocation> WIDE = new EnumMap<>(WatchTier.class);
  private static final EnumMap<WatchTier, ModelLayerLocation> SLIM = new EnumMap<>(WatchTier.class);

  static {
    for (WatchTier tier : WatchTier.values()) {
      String prefix = tier.name().toLowerCase(Locale.ROOT);
      WIDE.put(tier, createLayerLocation(prefix + "_watch_wide"));
      SLIM.put(tier, createLayerLocation(prefix + "_watch_slim"));
    }
  }

  public static ModelLayerLocation watch(WatchTier tier, boolean hasSlimArms) {
    return (hasSlimArms ? SLIM : WIDE).get(tier);
  }

  public static ModelLayerLocation createLayerLocation(String name) {
    return new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, name), name);
  }

  public static Supplier<LayerDefinition> layer(Supplier<MeshDefinition> mesh, int textureWidth, int textureHeight) {
    return () -> LayerDefinition.create(mesh.get(), textureWidth, textureHeight);
  }

  public static void register(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
    for (WatchTier tier : WatchTier.values()) {
      consumer.accept(WIDE.get(tier), layer(() -> RendererUtil.createWatchModel(false), 32, 32));
      consumer.accept(SLIM.get(tier), layer(() -> RendererUtil.createWatchModel(true), 32, 32));
    }
  }
}

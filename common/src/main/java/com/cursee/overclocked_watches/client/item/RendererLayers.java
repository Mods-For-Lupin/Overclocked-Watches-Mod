package com.cursee.overclocked_watches.client.item;

import com.cursee.overclocked_watches.Constants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RendererLayers {

    public static final ModelLayerLocation GOLDEN_WATCH_WIDE = createLayerLocation("golden_watch_wide");
    public static final ModelLayerLocation GOLDEN_WATCH_SLIM = createLayerLocation("golden_watch_slim");
    public static final ModelLayerLocation DIAMOND_WATCH_WIDE = createLayerLocation("diamond_watch_wide");
    public static final ModelLayerLocation DIAMOND_WATCH_SLIM = createLayerLocation("diamond_watch_slim");
    public static final ModelLayerLocation NETHERITE_WATCH_WIDE = createLayerLocation("netherite_watch_wide");
    public static final ModelLayerLocation NETHERITE_WATCH_SLIM = createLayerLocation("netherite_watch_slim");

    public static ModelLayerLocation goldenWatch(boolean hasSlimArms) {
        return hasSlimArms ? GOLDEN_WATCH_SLIM : GOLDEN_WATCH_WIDE;
    }

    public static ModelLayerLocation diamondWatch(boolean hasSlimArms) {
        return hasSlimArms ? DIAMOND_WATCH_SLIM : DIAMOND_WATCH_WIDE;
    }

    public static ModelLayerLocation netheriteWatch(boolean hasSlimArms) {
        return hasSlimArms ? NETHERITE_WATCH_SLIM : NETHERITE_WATCH_WIDE;
    }

    public static ModelLayerLocation createLayerLocation(String name) {
        return new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, name), name);
    }

    public static Supplier<LayerDefinition> layer(Supplier<MeshDefinition> mesh, int textureWidth, int textureHeight) {
        return () -> LayerDefinition.create(mesh.get(), textureWidth, textureHeight);
    }
}

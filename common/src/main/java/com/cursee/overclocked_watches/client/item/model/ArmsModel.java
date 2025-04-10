package com.cursee.overclocked_watches.client.item.model;

import com.cursee.overclocked_watches.client.item.RendererLayers;
import com.cursee.overclocked_watches.client.item.RendererUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class ArmsModel extends HumanoidModel<LivingEntity> {

    public ArmsModel(ModelPart part, Function<ResourceLocation, RenderType> renderType) {
        super(part, renderType);
    }

    public ArmsModel(ModelPart part) {
        this(part, RenderType::entityCutoutNoCull);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(leftArm, rightArm);
    }

    public void renderArm(HumanoidArm handSide, PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        getArm(handSide).visible = true;
        getArm(handSide.getOpposite()).visible = false;
        renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static ArmsModel bakeGoldenWatchTextureOnModel(boolean hasSlimArms) {
        return new ArmsModel(RendererUtil.bakeLayer(RendererLayers.goldenWatch(hasSlimArms)));
    }

    public static ArmsModel bakeDiamondWatchTextureOnModel(boolean hasSlimArms) {
        return new ArmsModel(RendererUtil.bakeLayer(RendererLayers.diamondWatch(hasSlimArms)));
    }

    public static ArmsModel bakeNetheriteWatchTextureOnModel(boolean hasSlimArms) {
        return new ArmsModel(RendererUtil.bakeLayer(RendererLayers.netheriteWatch(hasSlimArms)));
    }
}

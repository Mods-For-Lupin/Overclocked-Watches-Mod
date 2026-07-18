package io.github.jason13official.overclocked_watches.impl.client.item.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererUtil;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import java.util.function.Function;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class ArmsModel extends HumanoidModel<LivingEntity> {

  public ArmsModel(ModelPart part, Function<ResourceLocation, RenderType> renderType) {
    super(part, renderType);
  }

  public ArmsModel(ModelPart part) {
    this(part, RenderType::entityCutoutNoCull);
  }

  public static ArmsModel bakeWatchTextureOnModel(WatchTier tier, boolean hasSlimArms) {
    return new ArmsModel(RendererUtil.bakeLayer(RendererLayers.watch(tier, hasSlimArms)));
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
}

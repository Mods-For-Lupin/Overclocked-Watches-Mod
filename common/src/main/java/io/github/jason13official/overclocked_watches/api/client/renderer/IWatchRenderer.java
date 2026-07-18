package io.github.jason13official.overclocked_watches.api.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.overclocked_watches.OverclockedWatches;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IWatchRenderer {

  static ResourceLocation getTexturePath(String... names) {
    StringBuilder path = new StringBuilder("textures/entity/wearable");
    for (String name : names) {
      path.append('/');
      path.append(name);
    }
    path.append(".png");
    return OverclockedWatches.identifier(path.toString());
  }

  static void followBodyRotations(final LivingEntity livingEntity, final HumanoidModel<LivingEntity> model) {

    EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(livingEntity);

    if (!(renderer instanceof LivingEntityRenderer)) {
      return;
    }

    @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) renderer;
    EntityModel<LivingEntity> entityModel = livingRenderer.getModel();

    if (!(entityModel instanceof HumanoidModel<LivingEntity> bipedModel)) {
      return;
    }

    bipedModel.copyPropertiesTo(model);
  }

  void render(
      ItemStack stack, LivingEntity entity,
      int slotIndex,
      PoseStack poseStack, MultiBufferSource multiBufferSource,
      int light,
      float limbSwing, float limbSwingAmount,
      float partialTicks, float ageInTicks,
      float netHeadYaw, float headPitch);
}

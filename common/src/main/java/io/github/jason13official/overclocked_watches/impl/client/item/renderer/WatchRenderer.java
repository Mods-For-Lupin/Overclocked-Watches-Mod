package io.github.jason13official.overclocked_watches.impl.client.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jason13official.overclocked_watches.impl.client.item.model.ArmsModel;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WatchRenderer implements IWatchRenderer {

  private final ResourceLocation wideTexture;
  private final ResourceLocation slimTexture;
  private final ArmsModel wideModel;
  private final ArmsModel slimModel;

  public WatchRenderer(String name, Function<Boolean, ArmsModel> model) {
    this(IWatchRenderer.getTexturePath(name, "%s_wide".formatted(name)), IWatchRenderer.getTexturePath(name, "%s_slim".formatted(name)), model);
  }

  public WatchRenderer(String wideTexture, String slimTexture, Function<Boolean, ArmsModel> model) {
    this(IWatchRenderer.getTexturePath(wideTexture), IWatchRenderer.getTexturePath(slimTexture), model);
  }

  public WatchRenderer(ResourceLocation wideTexture, ResourceLocation slimTexture, Function<Boolean, ArmsModel> model) {
    this.wideTexture = wideTexture;
    this.slimTexture = slimTexture;
    this.wideModel = model.apply(false);
    this.slimModel = model.apply(true);
  }

  public static void registerAll() {
    Services.PLATFORM.registerWatchRenderer(ModItems.GOLDEN_WATCH, () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
    Services.PLATFORM.registerWatchRenderer(ModItems.DIAMOND_WATCH, () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
    Services.PLATFORM.registerWatchRenderer(ModItems.NETHERITE_WATCH, () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
  }

  @Nullable
  public static WatchRenderer getGloveRenderer(ItemStack stack) {
    if (!stack.isEmpty() && Services.PLATFORM.getWatchRenderer(stack.getItem()) instanceof WatchRenderer gloveRenderer) {
      return gloveRenderer;
    }
    return null;
  }

  protected static boolean hasSlimArms(Entity entity) {
    return entity instanceof AbstractClientPlayer player && player.getModelName().equals("slim");
  }

  protected ResourceLocation getTexture(boolean hasSlimArms) {
    return hasSlimArms ? slimTexture : wideTexture;
  }

  protected ArmsModel getModel(boolean hasSlimArms) {
    return hasSlimArms ? slimModel : wideModel;
  }

  @Override
  public void render(
      ItemStack stack,
      LivingEntity entity,
      int slotIndex,
      PoseStack poseStack,
      MultiBufferSource multiBufferSource,
      int light,
      float limbSwing,
      float limbSwingAmount,
      float partialTicks,
      float ageInTicks,
      float netHeadYaw,
      float headPitch
  ) {
    boolean hasSlimArms = hasSlimArms(entity);
    ArmsModel model = getModel(hasSlimArms);
    InteractionHand hand = slotIndex % 2 == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    HumanoidArm handSide = hand == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();

    model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
    IWatchRenderer.followBodyRotations(entity, model);

    renderArm(model, poseStack, multiBufferSource, handSide, light, hasSlimArms, stack.hasFoil());
  }

  protected void renderArm(ArmsModel model, PoseStack matrixStack, MultiBufferSource buffer, HumanoidArm handSide, int light, boolean hasSlimArms, boolean hasFoil) {
    RenderType renderType = model.renderType(getTexture(hasSlimArms));
    VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, renderType, false, hasFoil);
    model.renderArm(handSide, matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
  }

  public final void renderFirstPersonArm(PoseStack matrixStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, HumanoidArm side, boolean hasFoil) {
    if (!player.isSpectator()) {
      boolean hasSlimArms = hasSlimArms(player);
      ArmsModel model = getModel(hasSlimArms);

      ModelPart arm = side == HumanoidArm.LEFT ? model.leftArm : model.rightArm;

      model.setAllVisible(false);
      arm.visible = true;

      model.crouching = false;
      model.attackTime = model.swimAmount = 0;
      model.setupAnim(player, 0, 0, 0, 0, 0);
      arm.xRot = 0;

      renderFirstPersonArm(model, arm, matrixStack, buffer, light, hasSlimArms, hasFoil);
    }
  }

  protected void renderFirstPersonArm(ArmsModel model, ModelPart arm, PoseStack matrixStack, MultiBufferSource buffer, int light, boolean hasSlimArms, boolean hasFoil) {
    RenderType renderType = model.renderType(getTexture(hasSlimArms));
    VertexConsumer builder = ItemRenderer.getFoilBuffer(buffer, renderType, false, hasFoil);
    arm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
  }
}

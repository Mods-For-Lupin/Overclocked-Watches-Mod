package io.github.jason13official.overclocked_watches.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class FabricPlayerRendererMixin {

  @Unique
  private static void renderArm(PoseStack matrixStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, HumanoidArm handSide) {

    String groupId = handSide == player.getMainArm() ? "hand" : "offhand";
    TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
      for (Tuple<SlotReference, ItemStack> pair : component.getAllEquipped()) {
        ItemStack stack = pair.getB();
        if (pair.getA().inventory().getSlotType().getGroup().equals(groupId)
            && stack.getItem() instanceof WatchItem) {
          WatchRenderer gloveRenderer = WatchRenderer.getGloveRenderer(stack);
          if (gloveRenderer != null) {
            gloveRenderer.renderFirstPersonArm(matrixStack, buffer, light, player, handSide, stack.hasFoil());
          }
        }
      }
    });
  }

  @Inject(method = "renderLeftHand", at = @At("TAIL"))
  private void renderLeftGlove(PoseStack matrixStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, CallbackInfo callbackInfo) {
    renderArm(matrixStack, buffer, light, player, HumanoidArm.LEFT);
  }

  @Inject(method = "renderRightHand", at = @At("TAIL"))
  private void renderRightGlove(PoseStack matrixStack, MultiBufferSource buffer, int light, AbstractClientPlayer player, CallbackInfo callbackInfo) {
    renderArm(matrixStack, buffer, light, player, HumanoidArm.RIGHT);
  }
}

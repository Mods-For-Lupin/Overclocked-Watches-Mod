package io.github.jason13official.overclocked_watches.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jason13official.overclocked_watches.api.client.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.services.IPlatformHelper;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Builder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ForgePlatformHelper implements IPlatformHelper {

  @Override
  public String getPlatformName() {

    return "Forge";
  }

  @Override
  public boolean isModLoaded(String modId) {

    return ModList.get().isLoaded(modId);
  }

  @Override
  public boolean isDevelopmentEnvironment() {

    return !FMLLoader.isProduction();
  }

  @Override
  public Path getGameDirectory() {

    return FMLPaths.GAMEDIR.get();
  }

  @Override
  public Builder tabBuilder() {

    return CreativeModeTab.builder();
  }

  @Override
  public boolean isClientSide() {
    return FMLLoader.getDist().isClient();
  }

  @Override
  public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
    CuriosRendererRegistry.register(item, () -> new WatchCurioRenderer(rendererSupplier.get()));
  }

  @Override
  public IWatchRenderer getWatchRenderer(Item item) {
    Optional<ICurioRenderer> renderer = CuriosRendererRegistry.getRenderer(item);
    if (renderer.isPresent() && renderer.get() instanceof WatchCurioRenderer curioRenderer) {
      return curioRenderer.renderer();
    }
    return null;
  }

  @Override
  public boolean playerHasWatchEquipped(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    final AtomicBoolean foundWatch = new AtomicBoolean(false);
    CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
      if (iCuriosItemHandler.isEquipped(watch)) {
        foundWatch.set(true);
      }
    });

    return foundWatch.get();
  }

  @Override
  public ItemStack getEquippedWatch(Player player, WatchTier tier) {

    Item watch = ModItems.getWatch(tier);
    AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
    CuriosApi.getCuriosInventory(player)
        .ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(watch).ifPresent(slotResult -> itemStackReference.set(slotResult.stack())));

    return itemStackReference.get();
  }

  @Override
  public Item getItemFromRL(ResourceLocation rl) {
    return ForgeRegistries.ITEMS.getValue(rl);
  }

  @Override
  public ResourceLocation getRLFromItem(Item item) {
    return ForgeRegistries.ITEMS.getKey(item);
  }

  @Override
  public CompoundTag getPersistentData(Entity entity) {
    return entity.getPersistentData();
  }

  private record WatchCurioRenderer(IWatchRenderer renderer) implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent,
        MultiBufferSource multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      renderer.render(stack, slotContext.entity(), slotContext.index(), poseStack, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
  }
}
package io.github.jason13official.overclocked_watches.platform;

import io.github.jason13official.overclocked_watches.impl.client.item.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.services.IPlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import java.nio.file.Path;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

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
        if (renderer.isPresent() && renderer.get() instanceof WatchCurioRenderer artifactTrinketRenderer) {
            return artifactTrinketRenderer.renderer();
        }
        return null;
    }

    @Override
    public boolean playerHasNetheriteWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItems.NETHERITE_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasDiamondWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItems.DIAMOND_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasGoldenWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            if (iCuriosItemHandler.isEquipped(ModItems.GOLDEN_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public ItemStack getEquippedNetheriteWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(ModItems.NETHERITE_WATCH).ifPresent(slotResult -> itemStackReference.set(slotResult.stack())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedDiamondWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(ModItems.DIAMOND_WATCH).ifPresent(slotResult -> itemStackReference.set(slotResult.stack())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedGoldenWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(ModItems.GOLDEN_WATCH).ifPresent(slotResult -> itemStackReference.set(slotResult.stack())));

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

    private record WatchCurioRenderer(IWatchRenderer renderer) implements ICurioRenderer {

        @Override
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource multiBufferSource, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            renderer.render(stack, slotContext.entity(), slotContext.index(), poseStack, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
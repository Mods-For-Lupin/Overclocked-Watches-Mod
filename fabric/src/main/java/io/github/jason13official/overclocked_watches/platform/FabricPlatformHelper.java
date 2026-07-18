package io.github.jason13official.overclocked_watches.platform;

import io.github.jason13official.overclocked_watches.client.item.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.core.registry.ModItems;
import io.github.jason13official.overclocked_watches.platform.services.IPlatformHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getGameDirectory() {

        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public boolean isClientSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier) {
        TrinketRendererRegistry.registerRenderer(item, new WatchTrinketRenderer(rendererSupplier.get()));
    }

    @Override
    public <T extends Item> IWatchRenderer getWatchRenderer(T item) {
        Optional<TrinketRenderer> renderer = TrinketRendererRegistry.getRenderer(item);
        if (renderer.isPresent() && renderer.get() instanceof WatchTrinketRenderer artifactTrinketRenderer) {
            return artifactTrinketRenderer.renderer();
        }
        return null;
    }

    @Override
    public boolean playerHasNetheriteWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItems.NETHERITE_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasDiamondWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItems.DIAMOND_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public boolean playerHasGoldenWatchEquipped(Player player) {

        final AtomicBoolean foundWatch = new AtomicBoolean(false);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
            if (trinketComponent.isEquipped(itemStack -> itemStack.getItem() == ModItems.GOLDEN_WATCH)) foundWatch.set(true);
        });

        return foundWatch.get();
    }

    @Override
    public ItemStack getEquippedNetheriteWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItems.NETHERITE_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedDiamondWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItems.DIAMOND_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public ItemStack getEquippedGoldenWatch(Player player) {

        AtomicReference<ItemStack> itemStackReference = new AtomicReference<ItemStack>(ItemStack.EMPTY);
        TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> trinketComponent.getEquipped(ModItems.GOLDEN_WATCH).forEach(slotReferenceItemStackTuple -> itemStackReference.set(slotReferenceItemStackTuple.getB())));

        return itemStackReference.get();
    }

    @Override
    public Item getItemFromRL(ResourceLocation rl) {
        return BuiltInRegistries.ITEM.get(rl);
    }

    @Override
    public ResourceLocation getRLFromItem(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private record WatchTrinketRenderer(IWatchRenderer renderer) implements TrinketRenderer {

        @Override
        public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> entityModel, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            int index = slotReference.index() + (slotReference.inventory().getSlotType().getGroup().equals("hand") ? 0 : 1);
            renderer.render(stack, entity, index, poseStack, multiBufferSource, light, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}

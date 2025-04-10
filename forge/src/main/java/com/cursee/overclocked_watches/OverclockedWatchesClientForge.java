package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.client.ArmRenderHandler;
import com.cursee.overclocked_watches.client.KeyInputHandlerForge;
import com.cursee.overclocked_watches.client.item.RendererLayers;
import com.cursee.overclocked_watches.client.item.RendererUtil;
import com.cursee.overclocked_watches.client.item.model.ArmsModel;
import com.cursee.overclocked_watches.client.item.renderer.WatchRenderer;
import com.cursee.overclocked_watches.core.registry.ModItems;
import com.cursee.overclocked_watches.core.registry.ModParticles;
import com.cursee.overclocked_watches.core.world.particle.WatchGrowthParticle;
import com.cursee.overclocked_watches.mixin.LivingEntityRendererAccessor;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.client.render.CuriosLayer;

import java.util.Set;

public class OverclockedWatchesClientForge {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                OverclockedWatchesClient.init();
            });
        }
    }

    // @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    // public static class ForgeModBusEvents {}

    public OverclockedWatchesClientForge(IEventBus modEventBus) {

        modEventBus.addListener(this::onRegisterEntityLayerDefinitions);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onAddEntityRendererLayers);
        modEventBus.addListener(this::onRegisterParticleProviders);
        modEventBus.addListener(this::onRegisterKeyMappings);

        ArmRenderHandler.setup();
    }

    public void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Services.PLATFORM.registerWatchRenderer(ModItems.GOLDEN_WATCH, () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItems.DIAMOND_WATCH, () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItems.NETHERITE_WATCH, () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
        });
    }

    public void onAddEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
        Set<EntityType<?>> entities = Set.of(EntityType.ZOMBIE,
                EntityType.HUSK,
                EntityType.DROWNED,
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.ZOMBIFIED_PIGLIN);
        loop:
        for (EntityType<?> entity : entities) {
            EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entity);
            if (renderer == null) {
                continue;
            }
            LivingEntityRenderer livingEntityRenderer = (LivingEntityRenderer<?, ?>) renderer;
            for (RenderLayer<?, ?> layer : ((LivingEntityRendererAccessor<?, ?>) livingEntityRenderer).getLayers()) {
                if (layer instanceof CuriosLayer<?, ?>) {
                    continue loop;
                }
            }
            livingEntityRenderer.addLayer(new CuriosLayer<>(livingEntityRenderer));
        }
    }

    public void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.GOLDEN_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        event.registerSpriteSet(ModParticles.DIAMOND_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        event.registerSpriteSet(ModParticles.NETHERITE_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyInputHandlerForge.dayNightKey);
    }
}

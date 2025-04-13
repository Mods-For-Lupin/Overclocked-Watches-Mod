package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.client.entity.renderer.TrinketRenderers;
import com.cursee.overclocked_watches.client.item.RendererLayers;
import com.cursee.overclocked_watches.client.item.RendererUtil;
import com.cursee.overclocked_watches.client.network.packet.FabricConfigSyncClientHandler;
import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.network.FabricNetwork;
import com.cursee.overclocked_watches.core.registry.ModParticles;
import com.cursee.overclocked_watches.core.util.TimeManager;
import com.cursee.overclocked_watches.core.world.particle.WatchGrowthParticle;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.server.packs.PackType;

public class OverclockedWatchesClientFabric implements ClientModInitializer {

    public static KeyMapping dayNightKey;

    @Override
    public void onInitializeClient() {
        OverclockedWatchesClient.init();

        ClientPlayNetworking.registerGlobalReceiver(FabricNetwork.Packets.CONFIG_SYNC_S2C, FabricConfigSyncClientHandler::registerS2CPacketHandler);

        OverclockedWatchesClientFabric.registerModelLayers(); // here

        ParticleFactoryRegistry.getInstance().register(ModParticles.GOLDEN_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.DIAMOND_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.NETHERITE_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new TrinketRenderers());

        dayNightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                Constants.KEY_DAY_NIGHT,
                InputConstants.Type.KEYSYM,
                Math.toIntExact(CommonConfigValues.default_day_night_key),
                Constants.KEY_CATEGORY_DAY_NIGHT));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dayNightKey.consumeClick()) {
                ClientPlayNetworking.send(FabricNetwork.Packets.DAY_NIGHT_C2S, PacketByteBufs.create());
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(clientLevel -> {
            if (CommonConfigValues.use_long_time_delta && TimeManager.shouldOperate()) TimeManager.operate(clientLevel);
        });
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.GOLDEN_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.GOLDEN_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());

        EntityModelLayerRegistry.registerModelLayer(RendererLayers.DIAMOND_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.DIAMOND_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());

        EntityModelLayerRegistry.registerModelLayer(RendererLayers.NETHERITE_WATCH_WIDE, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32).get());
        EntityModelLayerRegistry.registerModelLayer(RendererLayers.NETHERITE_WATCH_SLIM, () -> RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32).get());
    }
}

package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.client.entity.renderer.TrinketRenderers;
import io.github.jason13official.overclocked_watches.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.client.item.RendererUtil;
import io.github.jason13official.overclocked_watches.client.network.packet.FabricConfigSyncClientHandler;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.core.network.FabricNetwork;
import io.github.jason13official.overclocked_watches.core.registry.ModItems;
import io.github.jason13official.overclocked_watches.core.registry.ModParticles;
import io.github.jason13official.overclocked_watches.core.util.TimeManager;
import io.github.jason13official.overclocked_watches.core.world.particle.WatchGrowthParticle;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

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
                Math.toIntExact(ServerModConfig.defaultDayNightKey),
                Constants.KEY_CATEGORY_DAY_NIGHT));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (dayNightKey.consumeClick()) {

                ClientLevel level = client.level;
                LocalPlayer player = client.player;
                if (level == null || player == null) return;

                long pAmount = ServerModConfig.goldenTimeAdvancementTicks;

                Item item = client.player.getMainHandItem().getItem();
                if (item == ModItems.DIAMOND_WATCH) pAmount = ServerModConfig.diamondTimeAdvancementTicks;
                if (item == ModItems.NETHERITE_WATCH) pAmount = ServerModConfig.netheriteTimeAdvancementTicks;

                if (!ServerModConfig.useLongTimeDelta) {
                    level.setDayTime(level.getDayTime() + pAmount);
                }
                else {
                    TimeManager.CLIENT.addToRemainingTime((int) pAmount);
                }

                // player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
                // player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
                player.playSound(SoundEvents.BELL_BLOCK, 1, 1);
                player.playSound(SoundEvents.BELL_RESONATE, 1, 1);

                System.out.println("sending packet");
                ClientPlayNetworking.send(FabricNetwork.Packets.DAY_NIGHT_C2S, PacketByteBufs.create());
            }
        });

        ClientTickEvents.START_WORLD_TICK.register(clientLevel -> {
            if (ServerModConfig.useLongTimeDelta && TimeManager.CLIENT.shouldOperate()) TimeManager.CLIENT.operate(clientLevel);
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

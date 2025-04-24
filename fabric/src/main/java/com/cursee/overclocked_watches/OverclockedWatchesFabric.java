package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.FabricCommonConfigHandler;
import com.cursee.overclocked_watches.core.network.FabricNetwork;
import com.cursee.overclocked_watches.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.overclocked_watches.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.core.util.IEntityDataSaver;
import com.cursee.overclocked_watches.core.util.OverclockedWatchesUtil;
import com.cursee.overclocked_watches.core.util.TimeManager;
import com.cursee.overclocked_watches.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class OverclockedWatchesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        FabricCommonConfigHandler.onLoad();
        RegistryFabric.register();
        FabricNetwork.Packets.registerPacketIDsAndReceivers();

        ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::registerS2CPacketSender);

//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//            OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) handler.player).getPersistentData(), handler.player);
//        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ServerPlayer player)) return;
            OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) player).getPersistentData(), player);
        });

//        ServerPlayerEvents.AFTER_RESPAWN.register((entity, world, isAlive) -> {
//            // if (!(entity instanceof Player player)) return;
//            // OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) player).getPersistentData(), player);
//        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, isAlive) -> {
            // if (!isAlive) return;
            CompoundTag temp = new CompoundTag();
            OverclockedWatchesUtil.saveCooldowns(temp, oldPlayer);
            OverclockedWatchesUtil.loadCooldowns(temp, newPlayer);
        });

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // if (server.getTickCount() % 2 != 0) return;
            if (TimeManager.SERVER == null) TimeManager.SERVER = new TimeManager();
            if (CommonConfigValues.use_long_time_delta && TimeManager.SERVER.shouldOperate()) TimeManager.SERVER.operate(server.overworld());
        });
    }
}

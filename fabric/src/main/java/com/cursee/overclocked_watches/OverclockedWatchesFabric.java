package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.FabricCommonConfigHandler;
import com.cursee.overclocked_watches.core.network.FabricNetwork;
import com.cursee.overclocked_watches.core.network.packet.FabricConfigSyncS2CPacket;
import com.cursee.overclocked_watches.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.core.util.TimeManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class OverclockedWatchesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        FabricCommonConfigHandler.onLoad();
        RegistryFabric.register();
        FabricNetwork.Packets.registerPacketIDsAndReceivers();

        ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::registerS2CPacketSender);

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            // if (server.getTickCount() % 2 != 0) return;
            if (CommonConfigValues.use_long_time_delta && TimeManager.shouldOperate()) TimeManager.operate(server.overworld());
        });

        ClientTickEvents.START_WORLD_TICK.register(clientLevel -> {
            if (CommonConfigValues.use_long_time_delta && TimeManager.shouldOperate()) TimeManager.operate(clientLevel);
        });
    }
}

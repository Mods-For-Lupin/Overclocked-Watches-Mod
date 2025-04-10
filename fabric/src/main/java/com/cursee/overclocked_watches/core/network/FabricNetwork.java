package com.cursee.overclocked_watches.core.network;

import com.cursee.overclocked_watches.OverclockedWatches;
import com.cursee.overclocked_watches.core.network.packet.FabricDayNightC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class FabricNetwork {

    public static class Packets {

        public static final ResourceLocation DAY_NIGHT = OverclockedWatches.identifier("day_night");

        public static void registerC2SReceivers() {
            ServerPlayNetworking.registerGlobalReceiver(DAY_NIGHT, FabricDayNightC2SPacket::handle);
        }
    }
}

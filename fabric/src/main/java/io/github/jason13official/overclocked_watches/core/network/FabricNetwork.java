package io.github.jason13official.overclocked_watches.core.network;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.core.network.packet.FabricDayNightC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork {

  public static void sendToPlayer(FriendlyByteBuf data, ServerPlayer player, ResourceLocation packetID) {
    ServerPlayNetworking.send(player, packetID, data);
  }

  public static class Packets {

    public static final ResourceLocation DAY_NIGHT_C2S = OverclockedWatches.identifier("day_night");
    public static final ResourceLocation CONFIG_SYNC_S2C = OverclockedWatches.identifier("config_sync");

    public static void registerPacketIDsAndReceivers() {
      ServerPlayNetworking.registerGlobalReceiver(DAY_NIGHT_C2S, FabricDayNightC2SPacket::handle);
    }
  }
}

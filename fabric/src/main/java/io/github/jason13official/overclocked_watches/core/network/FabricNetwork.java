package io.github.jason13official.overclocked_watches.core.network;

import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.DayNightC2SHandler;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.DayNightC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork {

  public static void sendToPlayer(ConfigSyncPayload payload, ServerPlayer player) {
    ServerPlayNetworking.send(player, payload);
  }

  public static class Packets {

    public static void registerPacketIDsAndReceivers() {

      PayloadTypeRegistry.playC2S().register(DayNightC2SPayload.TYPE, DayNightC2SPayload.STREAM_CODEC);
      PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.STREAM_CODEC);

      ServerPlayNetworking.registerGlobalReceiver(DayNightC2SPayload.TYPE,
          (payload, context) -> DayNightC2SHandler.handle(context.server(), context.player()));
    }
  }
}

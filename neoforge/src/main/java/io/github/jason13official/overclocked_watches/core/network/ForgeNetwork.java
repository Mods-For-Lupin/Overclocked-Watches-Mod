package io.github.jason13official.overclocked_watches.core.network;

import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.DayNightC2SHandler;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.DayNightC2SPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ForgeNetwork {

  public static void register(RegisterPayloadHandlersEvent event) {

    PayloadRegistrar registrar = event.registrar("1.0");

    registrar.playToServer(DayNightC2SPayload.TYPE, DayNightC2SPayload.STREAM_CODEC,
        (payload, context) -> {
          ServerPlayer player = (ServerPlayer) context.player();
          DayNightC2SHandler.handle(player.getServer(), player);
        });

    registrar.playToClient(ConfigSyncPayload.TYPE, ConfigSyncPayload.STREAM_CODEC,
        (payload, context) -> payload.applyToConfig());
  }

  public static void sendToPlayer(ConfigSyncPayload payload, ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, payload);
  }

  public static void sendToServer(CustomPacketPayload payload) {
    PacketDistributor.sendToServer(payload);
  }
}

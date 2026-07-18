package io.github.jason13official.overclocked_watches.core.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricDayNightC2SPacket {

  public static void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf packetData, PacketSender sender) {
    DayNightC2SHandler.handle(server, player);
  }
}

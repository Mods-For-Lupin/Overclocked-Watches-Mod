package io.github.jason13official.overclocked_watches.client.network.packet;

import io.github.jason13official.overclocked_watches.core.network.packet.ConfigSyncPayload;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

  public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
    ConfigSyncPayload.read(data).applyToConfig();
  }
}

package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.core.network.FabricNetwork;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FabricConfigSyncS2CPacket {

  public static void registerS2CPacketSender(Entity entity, Level level) {
    if (!(entity instanceof ServerPlayer player)) {
      return;
    }
    FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());

    ConfigSyncPayload.fromServerConfig().write(data);

    FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
  }

}

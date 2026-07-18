package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.core.network.FabricNetwork;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FabricConfigSyncS2CPacket {

  public static void registerS2CPacketSender(Entity entity, Level level) {
    if (!(entity instanceof ServerPlayer player)) {
      return;
    }

    FabricNetwork.sendToPlayer(ConfigSyncPayload.fromServerConfig(), player);
  }

}

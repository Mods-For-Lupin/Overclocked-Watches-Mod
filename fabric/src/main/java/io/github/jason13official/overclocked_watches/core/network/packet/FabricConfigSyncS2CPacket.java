package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.core.network.FabricNetwork;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
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

    data.writeBoolean(ServerModConfig.dayNightCycleAllowed); // 0
    data.writeLong(ServerModConfig.defaultDayNightKey); // 1
    data.writeBoolean(ServerModConfig.useLongTimeDelta); // 2
    data.writeLong(ServerModConfig.longTimeDelta); // 3
    data.writeLong(ServerModConfig.goldenWatchDurability); // 4
    data.writeLong(ServerModConfig.diamondWatchDurability); // 5
    data.writeLong(ServerModConfig.netheriteWatchDurability); // 6
    data.writeLong(ServerModConfig.goldenWatchCharges); // 7
    data.writeLong(ServerModConfig.diamondWatchCharges); // 8
    data.writeLong(ServerModConfig.netheriteWatchCharges); // 9
    data.writeLong(ServerModConfig.goldenWatchCooldownMinutes); // 10
    data.writeLong(ServerModConfig.diamondWatchCooldownMinutes); // 11
    data.writeLong(ServerModConfig.netheriteWatchCooldownMinutes); // 12
    data.writeLong(ServerModConfig.goldenTimeAdvancementTicks); // 13
    data.writeLong(ServerModConfig.diamondTimeAdvancementTicks); // 14
    data.writeLong(ServerModConfig.netheriteTimeAdvancementTicks); // 15

    FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
  }

}

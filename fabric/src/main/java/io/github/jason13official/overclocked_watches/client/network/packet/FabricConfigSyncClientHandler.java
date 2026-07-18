package io.github.jason13official.overclocked_watches.client.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

    public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        ServerModConfig.dayNightCycleAllowed = data.readBoolean();
        ServerModConfig.defaultDayNightKey = data.readLong();
        ServerModConfig.useLongTimeDelta = data.readBoolean();
        ServerModConfig.longTimeDelta = data.readLong();
        ServerModConfig.goldenWatchDurability = data.readLong();
        ServerModConfig.diamondWatchDurability = data.readLong();
        ServerModConfig.netheriteWatchDurability = data.readLong();
        ServerModConfig.goldenWatchCharges = data.readLong();
        ServerModConfig.diamondWatchCharges = data.readLong();
        ServerModConfig.netheriteWatchCharges = data.readLong();
        ServerModConfig.goldenWatchCooldownMinutes = data.readLong();
        ServerModConfig.diamondWatchCooldownMinutes = data.readLong();
        ServerModConfig.netheriteWatchCooldownMinutes = data.readLong();
        ServerModConfig.goldenTimeAdvancementTicks = data.readLong();
        ServerModConfig.diamondTimeAdvancementTicks = data.readLong();
        ServerModConfig.netheriteTimeAdvancementTicks = data.readLong();
    }
}

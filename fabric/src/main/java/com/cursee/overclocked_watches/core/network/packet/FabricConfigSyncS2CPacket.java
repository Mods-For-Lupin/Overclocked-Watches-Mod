package com.cursee.overclocked_watches.core.network.packet;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.network.FabricNetwork;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class FabricConfigSyncS2CPacket {

    public static void registerS2CPacketSender(Entity entity, Level level) {
        if (!(entity instanceof ServerPlayer)) return;
        ServerPlayer player = (ServerPlayer) entity;
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());

        data.writeBoolean(CommonConfigValues.day_night_cycle_allowed); // 0
        data.writeLong(CommonConfigValues.default_day_night_key); // 1
        data.writeBoolean(CommonConfigValues.use_long_time_delta); // 2
        data.writeLong(CommonConfigValues.long_time_delta); // 3
        data.writeLong(CommonConfigValues.golden_watch_durability); // 4
        data.writeLong(CommonConfigValues.diamond_watch_durability); // 5
        data.writeLong(CommonConfigValues.netherite_watch_durability); // 6
        data.writeLong(CommonConfigValues.golden_watch_charges); // 7
        data.writeLong(CommonConfigValues.diamond_watch_charges); // 8
        data.writeLong(CommonConfigValues.netherite_watch_charges); // 9
        data.writeLong(CommonConfigValues.golden_watch_cooldown_minutes); // 10
        data.writeLong(CommonConfigValues.diamond_watch_cooldown_minutes); // 11
        data.writeLong(CommonConfigValues.netherite_watch_cooldown_minutes); // 12
        data.writeLong(CommonConfigValues.golden_time_advancement_ticks); // 13
        data.writeLong(CommonConfigValues.diamond_time_advancement_ticks); // 14
        data.writeLong(CommonConfigValues.netherite_time_advancement_ticks); // 15

        FabricNetwork.sendToPlayer(data, player, FabricNetwork.Packets.CONFIG_SYNC_S2C);
    }

}

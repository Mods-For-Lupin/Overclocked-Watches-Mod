package com.cursee.overclocked_watches.client.network.packet;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class FabricConfigSyncClientHandler {

    public static void registerS2CPacketHandler(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender responseSender) {
        CommonConfigValues.day_night_cycle_allowed = data.readBoolean();
        CommonConfigValues.default_day_night_key = data.readLong();
        CommonConfigValues.use_long_time_delta = data.readBoolean();
        CommonConfigValues.long_time_delta = data.readLong();
        CommonConfigValues.golden_watch_durability = data.readLong();
        CommonConfigValues.diamond_watch_durability = data.readLong();
        CommonConfigValues.netherite_watch_durability = data.readLong();
        CommonConfigValues.golden_watch_charges = data.readLong();
        CommonConfigValues.diamond_watch_charges = data.readLong();
        CommonConfigValues.netherite_watch_charges = data.readLong();
        CommonConfigValues.golden_watch_cooldown_minutes = data.readLong();
        CommonConfigValues.diamond_watch_cooldown_minutes = data.readLong();
        CommonConfigValues.netherite_watch_cooldown_minutes = data.readLong();
        CommonConfigValues.golden_time_advancement_ticks = data.readLong();
        CommonConfigValues.diamond_time_advancement_ticks = data.readLong();
        CommonConfigValues.netherite_time_advancement_ticks = data.readLong();
    }
}

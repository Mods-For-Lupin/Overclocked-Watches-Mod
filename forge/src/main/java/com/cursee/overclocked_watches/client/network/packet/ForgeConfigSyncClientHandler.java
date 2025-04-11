package com.cursee.overclocked_watches.client.network.packet;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncClientHandler {

    public static void registerS2CPacketHandler(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            CommonConfigValues.day_night_cycle_allowed = packet.day_night_cycle_allowed;
            CommonConfigValues.default_day_night_key = packet.default_day_night_key;
            CommonConfigValues.use_long_time_delta = packet.use_long_time_delta;
            CommonConfigValues.long_time_delta = packet.long_time_delta;
            CommonConfigValues.golden_watch_durability = packet.golden_watch_durability;
            CommonConfigValues.diamond_watch_durability = packet.diamond_watch_durability;
            CommonConfigValues.netherite_watch_durability = packet.netherite_watch_durability;
            CommonConfigValues.golden_watch_charges = packet.golden_watch_charges;
            CommonConfigValues.diamond_watch_charges = packet.diamond_watch_charges;
            CommonConfigValues.netherite_watch_charges = packet.netherite_watch_charges;
            CommonConfigValues.golden_watch_cooldown_minutes = packet.golden_watch_cooldown_minutes;
            CommonConfigValues.diamond_watch_cooldown_minutes = packet.diamond_watch_cooldown_minutes;
            CommonConfigValues.netherite_watch_cooldown_minutes = packet.netherite_watch_cooldown_minutes;
            CommonConfigValues.golden_time_advancement_ticks = packet.golden_time_advancement_ticks;
            CommonConfigValues.diamond_time_advancement_ticks = packet.diamond_time_advancement_ticks;
            CommonConfigValues.netherite_time_advancement_ticks = packet.netherite_time_advancement_ticks;
        });
    }
}

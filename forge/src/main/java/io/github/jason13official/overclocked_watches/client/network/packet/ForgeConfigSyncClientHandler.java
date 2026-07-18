package io.github.jason13official.overclocked_watches.client.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncClientHandler {

    public static void registerS2CPacketHandler(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerModConfig.dayNightCycleAllowed = packet.day_night_cycle_allowed;
            ServerModConfig.defaultDayNightKey = packet.default_day_night_key;
            ServerModConfig.useLongTimeDelta = packet.use_long_time_delta;
            ServerModConfig.longTimeDelta = packet.long_time_delta;
            ServerModConfig.goldenWatchDurability = packet.golden_watch_durability;
            ServerModConfig.diamondWatchDurability = packet.diamond_watch_durability;
            ServerModConfig.netheriteWatchDurability = packet.netherite_watch_durability;
            ServerModConfig.goldenWatchCharges = packet.golden_watch_charges;
            ServerModConfig.diamondWatchCharges = packet.diamond_watch_charges;
            ServerModConfig.netheriteWatchCharges = packet.netherite_watch_charges;
            ServerModConfig.goldenWatchCooldownMinutes = packet.golden_watch_cooldown_minutes;
            ServerModConfig.diamondWatchCooldownMinutes = packet.diamond_watch_cooldown_minutes;
            ServerModConfig.netheriteWatchCooldownMinutes = packet.netherite_watch_cooldown_minutes;
            ServerModConfig.goldenTimeAdvancementTicks = packet.golden_time_advancement_ticks;
            ServerModConfig.diamondTimeAdvancementTicks = packet.diamond_time_advancement_ticks;
            ServerModConfig.netheriteTimeAdvancementTicks = packet.netherite_time_advancement_ticks;
        });
    }
}

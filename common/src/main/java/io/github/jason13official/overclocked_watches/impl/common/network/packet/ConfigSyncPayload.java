package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import net.minecraft.network.FriendlyByteBuf;

public record ConfigSyncPayload(boolean dayNightCycleAllowed, long defaultDayNightKey, boolean useLongTimeDelta, long longTimeDelta,
                                 long goldenWatchDurability, long diamondWatchDurability, long netheriteWatchDurability,
                                 long goldenWatchCharges, long diamondWatchCharges, long netheriteWatchCharges,
                                 long goldenWatchCooldownMinutes, long diamondWatchCooldownMinutes, long netheriteWatchCooldownMinutes,
                                 long goldenTimeAdvancementTicks, long diamondTimeAdvancementTicks, long netheriteTimeAdvancementTicks) {

  public static ConfigSyncPayload fromServerConfig() {
    return new ConfigSyncPayload(
        ServerModConfig.dayNightCycleAllowed, ServerModConfig.defaultDayNightKey, ServerModConfig.useLongTimeDelta, ServerModConfig.longTimeDelta,
        ServerModConfig.goldenWatchDurability, ServerModConfig.diamondWatchDurability, ServerModConfig.netheriteWatchDurability,
        ServerModConfig.goldenWatchCharges, ServerModConfig.diamondWatchCharges, ServerModConfig.netheriteWatchCharges,
        ServerModConfig.goldenWatchCooldownMinutes, ServerModConfig.diamondWatchCooldownMinutes, ServerModConfig.netheriteWatchCooldownMinutes,
        ServerModConfig.goldenTimeAdvancementTicks, ServerModConfig.diamondTimeAdvancementTicks, ServerModConfig.netheriteTimeAdvancementTicks
    );
  }

  public static ConfigSyncPayload read(FriendlyByteBuf data) {
    return new ConfigSyncPayload(
        data.readBoolean(), data.readLong(), data.readBoolean(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong()
    );
  }

  public void write(FriendlyByteBuf data) {
    data.writeBoolean(dayNightCycleAllowed);
    data.writeLong(defaultDayNightKey);
    data.writeBoolean(useLongTimeDelta);
    data.writeLong(longTimeDelta);
    data.writeLong(goldenWatchDurability);
    data.writeLong(diamondWatchDurability);
    data.writeLong(netheriteWatchDurability);
    data.writeLong(goldenWatchCharges);
    data.writeLong(diamondWatchCharges);
    data.writeLong(netheriteWatchCharges);
    data.writeLong(goldenWatchCooldownMinutes);
    data.writeLong(diamondWatchCooldownMinutes);
    data.writeLong(netheriteWatchCooldownMinutes);
    data.writeLong(goldenTimeAdvancementTicks);
    data.writeLong(diamondTimeAdvancementTicks);
    data.writeLong(netheriteTimeAdvancementTicks);
  }

  public void applyToConfig() {
    ServerModConfig.dayNightCycleAllowed = dayNightCycleAllowed;
    ServerModConfig.defaultDayNightKey = defaultDayNightKey;
    ServerModConfig.useLongTimeDelta = useLongTimeDelta;
    ServerModConfig.longTimeDelta = longTimeDelta;
    ServerModConfig.goldenWatchDurability = goldenWatchDurability;
    ServerModConfig.diamondWatchDurability = diamondWatchDurability;
    ServerModConfig.netheriteWatchDurability = netheriteWatchDurability;
    ServerModConfig.goldenWatchCharges = goldenWatchCharges;
    ServerModConfig.diamondWatchCharges = diamondWatchCharges;
    ServerModConfig.netheriteWatchCharges = netheriteWatchCharges;
    ServerModConfig.goldenWatchCooldownMinutes = goldenWatchCooldownMinutes;
    ServerModConfig.diamondWatchCooldownMinutes = diamondWatchCooldownMinutes;
    ServerModConfig.netheriteWatchCooldownMinutes = netheriteWatchCooldownMinutes;
    ServerModConfig.goldenTimeAdvancementTicks = goldenTimeAdvancementTicks;
    ServerModConfig.diamondTimeAdvancementTicks = diamondTimeAdvancementTicks;
    ServerModConfig.netheriteTimeAdvancementTicks = netheriteTimeAdvancementTicks;
  }
}

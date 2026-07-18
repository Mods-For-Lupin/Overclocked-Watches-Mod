package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import net.minecraft.network.FriendlyByteBuf;

public record ConfigSyncPayload(boolean dayNightCycleAllowed, boolean useLongTimeDelta, long longTimeDelta,
                                 long goldenWatchDurability, long diamondWatchDurability, long netheriteWatchDurability,
                                 long goldenWatchCharges, long diamondWatchCharges, long netheriteWatchCharges,
                                 long goldenWatchCooldownMinutes, long diamondWatchCooldownMinutes, long netheriteWatchCooldownMinutes,
                                 long goldenTimeAdvancementTicks, long diamondTimeAdvancementTicks, long netheriteTimeAdvancementTicks) {

  public static ConfigSyncPayload fromServerConfig() {
    return new ConfigSyncPayload(
        ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.get(),
        ServerModConfig.USE_LONG_TIME_DELTA.get(),
        ServerModConfig.LONG_TIME_DELTA.get(),
        ServerModConfig.GOLDEN_WATCH_DURABILITY.get(),
        ServerModConfig.DIAMOND_WATCH_DURABILITY.get(),
        ServerModConfig.NETHERITE_WATCH_DURABILITY.get(),
        ServerModConfig.GOLDEN_WATCH_CHARGES.get(),
        ServerModConfig.DIAMOND_WATCH_CHARGES.get(),
        ServerModConfig.NETHERITE_WATCH_CHARGES.get(),
        ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.get(),
        ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.get(),
        ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.get(),
        ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.get(),
        ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.get(),
        ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.get()
    );
  }

  public static ConfigSyncPayload read(FriendlyByteBuf data) {

    boolean dayNightCycleAllowed = data.readBoolean();
    boolean useLongTimeDelta = data.readBoolean();
    long longTimeDelta = data.readLong();
    long goldenWatchDurability = data.readLong();
    long diamondWatchDurability = data.readLong();
    long netheriteWatchDurability = data.readLong();
    long goldenWatchCharges = data.readLong();
    long diamondWatchCharges = data.readLong();
    long netheriteWatchCharges = data.readLong();
    long goldenWatchCooldownMinutes = data.readLong();
    long diamondWatchCooldownMinutes = data.readLong();
    long netheriteWatchCooldownMinutes = data.readLong();
    long goldenTimeAdvancementTicks = data.readLong();
    long diamondTimeAdvancementTicks = data.readLong();
    long netheriteTimeAdvancementTicks = data.readLong();

    return new ConfigSyncPayload(
        dayNightCycleAllowed,
        useLongTimeDelta,
        longTimeDelta,
        goldenWatchDurability,
        diamondWatchDurability,
        netheriteWatchDurability,
        goldenWatchCharges,
        diamondWatchCharges,
        netheriteWatchCharges,
        goldenWatchCooldownMinutes,
        diamondWatchCooldownMinutes,
        netheriteWatchCooldownMinutes,
        goldenTimeAdvancementTicks,
        diamondTimeAdvancementTicks,
        netheriteTimeAdvancementTicks
    );
  }

  public void write(FriendlyByteBuf data) {

    data.writeBoolean(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.get());
    data.writeBoolean(ServerModConfig.USE_LONG_TIME_DELTA.get());
    data.writeLong(ServerModConfig.LONG_TIME_DELTA.get());
    data.writeLong(ServerModConfig.GOLDEN_WATCH_DURABILITY.get());
    data.writeLong(ServerModConfig.DIAMOND_WATCH_DURABILITY.get());
    data.writeLong(ServerModConfig.NETHERITE_WATCH_DURABILITY.get());
    data.writeLong(ServerModConfig.GOLDEN_WATCH_CHARGES.get());
    data.writeLong(ServerModConfig.DIAMOND_WATCH_CHARGES.get());
    data.writeLong(ServerModConfig.NETHERITE_WATCH_CHARGES.get());
    data.writeLong(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.get());
    data.writeLong(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.get());
    data.writeLong(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.get());
    data.writeLong(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.get());
    data.writeLong(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.get());
    data.writeLong(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.get());
  }

  public void applyToConfig() {

    ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.set(dayNightCycleAllowed);
    ServerModConfig.USE_LONG_TIME_DELTA.set(useLongTimeDelta);
    ServerModConfig.LONG_TIME_DELTA.set(longTimeDelta);
    ServerModConfig.GOLDEN_WATCH_DURABILITY.set(goldenWatchDurability);
    ServerModConfig.DIAMOND_WATCH_DURABILITY.set(diamondWatchDurability);
    ServerModConfig.NETHERITE_WATCH_DURABILITY.set(netheriteWatchDurability);
    ServerModConfig.GOLDEN_WATCH_CHARGES.set(goldenWatchCharges);
    ServerModConfig.DIAMOND_WATCH_CHARGES.set(diamondWatchCharges);
    ServerModConfig.NETHERITE_WATCH_CHARGES.set(netheriteWatchCharges);
    ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.set(goldenWatchCooldownMinutes);
    ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.set(diamondWatchCooldownMinutes);
    ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.set(netheriteWatchCooldownMinutes);
    ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.set(goldenTimeAdvancementTicks);
    ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.set(diamondTimeAdvancementTicks);
    ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.set(netheriteTimeAdvancementTicks);
  }
}

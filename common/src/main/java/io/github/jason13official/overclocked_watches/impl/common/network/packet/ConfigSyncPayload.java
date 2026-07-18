package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig.TierConfig;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ConfigSyncPayload(boolean dayNightCycleAllowed, boolean useLongTimeDelta, long longTimeDelta,
                                 long[] durability, long[] charges, long[] cooldownMinutes, long[] timeAdvancementTicks) implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<ConfigSyncPayload> TYPE = new CustomPacketPayload.Type<>(OverclockedWatches.identifier("config_sync"));

  public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = CustomPacketPayload.codec(ConfigSyncPayload::write, ConfigSyncPayload::read);

  @Override
  public CustomPacketPayload.Type<ConfigSyncPayload> type() {
    return TYPE;
  }

  private static final int TIER_COUNT = WatchTier.values().length;

  public static ConfigSyncPayload fromServerConfig() {

    long[] durability = new long[TIER_COUNT];
    long[] charges = new long[TIER_COUNT];
    long[] cooldownMinutes = new long[TIER_COUNT];
    long[] timeAdvancementTicks = new long[TIER_COUNT];

    for (WatchTier tier : WatchTier.values()) {
      TierConfig tierConfig = ServerModConfig.get(tier);
      int i = tier.ordinal();
      durability[i] = tierConfig.durability().get();
      charges[i] = tierConfig.charges().get();
      cooldownMinutes[i] = tierConfig.cooldownMinutes().get();
      timeAdvancementTicks[i] = tierConfig.timeAdvancementTicks().get();
    }

    return new ConfigSyncPayload(
        ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.get(),
        ServerModConfig.USE_LONG_TIME_DELTA.get(),
        ServerModConfig.LONG_TIME_DELTA.get(),
        durability, charges, cooldownMinutes, timeAdvancementTicks
    );
  }

  public static ConfigSyncPayload read(FriendlyByteBuf data) {

    boolean dayNightCycleAllowed = data.readBoolean();
    boolean useLongTimeDelta = data.readBoolean();
    long longTimeDelta = data.readLong();
    long[] durability = readLongArray(data);
    long[] charges = readLongArray(data);
    long[] cooldownMinutes = readLongArray(data);
    long[] timeAdvancementTicks = readLongArray(data);

    return new ConfigSyncPayload(dayNightCycleAllowed, useLongTimeDelta, longTimeDelta, durability, charges, cooldownMinutes, timeAdvancementTicks);
  }

  private static long[] readLongArray(FriendlyByteBuf data) {

    long[] values = new long[TIER_COUNT];
    for (int i = 0; i < TIER_COUNT; i++) {
      values[i] = data.readLong();
    }
    return values;
  }

  private static void writeLongArray(FriendlyByteBuf data, long[] values) {

    for (long value : values) {
      data.writeLong(value);
    }
  }

  public void write(FriendlyByteBuf data) {

    data.writeBoolean(dayNightCycleAllowed);
    data.writeBoolean(useLongTimeDelta);
    data.writeLong(longTimeDelta);
    writeLongArray(data, durability);
    writeLongArray(data, charges);
    writeLongArray(data, cooldownMinutes);
    writeLongArray(data, timeAdvancementTicks);
  }

  public void applyToConfig() {

    ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.set(dayNightCycleAllowed);
    ServerModConfig.USE_LONG_TIME_DELTA.set(useLongTimeDelta);
    ServerModConfig.LONG_TIME_DELTA.set(longTimeDelta);

    for (WatchTier tier : WatchTier.values()) {
      TierConfig tierConfig = ServerModConfig.get(tier);
      int i = tier.ordinal();
      tierConfig.durability().set(durability[i]);
      tierConfig.charges().set(charges[i]);
      tierConfig.cooldownMinutes().set(cooldownMinutes[i]);
      tierConfig.timeAdvancementTicks().set(timeAdvancementTicks[i]);
    }
  }
}

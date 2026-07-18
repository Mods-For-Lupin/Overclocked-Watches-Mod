package io.github.jason13official.overclocked_watches.impl.common;

import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import java.util.EnumMap;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServerModConfig {

  public static boolean dayNightCycleAllowed = true;
  public static boolean useLongTimeDelta = true;
  public static long longTimeDelta = 100L; // 500 ? (also ever 5 ticks ????)

  public static final ConfigGetterSetter<Boolean> DAY_NIGHT_CYCLE_ALLOWED = new ConfigGetterSetter<>("day_night_cycle_allowed", () -> dayNightCycleAllowed, value -> dayNightCycleAllowed = value);
  public static final ConfigGetterSetter<Boolean> USE_LONG_TIME_DELTA = new ConfigGetterSetter<>("use_long_time_delta", () -> useLongTimeDelta, value -> useLongTimeDelta = value);
  public static final ConfigGetterSetter<Long> LONG_TIME_DELTA = new ConfigGetterSetter<>("long_time_delta", () -> longTimeDelta, value -> longTimeDelta = value);

  // per-tier defaults, indexed by WatchTier.ordinal(): {durability, charges, cooldownMinutes, timeAdvancementTicks}
  private static final long[][] TIER_DEFAULTS = {
      {100L, 1L, 20L, 12_000L}, // GOLDEN
      {300L, 3L, 10L, 12_000L}, // DIAMOND
      {500L, 5L, 5L, 12_000L},  // NETHERITE
  };

  private static final EnumMap<WatchTier, TierConfig> TIER_CONFIGS = buildTierConfigs();

  private static EnumMap<WatchTier, TierConfig> buildTierConfigs() {

    EnumMap<WatchTier, TierConfig> map = new EnumMap<>(WatchTier.class);
    for (WatchTier tier : WatchTier.values()) {

      long[] store = TIER_DEFAULTS[tier.ordinal()].clone();
      String prefix = tier.name().toLowerCase(Locale.ROOT);

      map.put(tier, new TierConfig(
          new ConfigGetterSetter<>(prefix + "_watch_durability", () -> store[0], value -> store[0] = value),
          new ConfigGetterSetter<>(prefix + "_watch_charges", () -> store[1], value -> store[1] = value),
          new ConfigGetterSetter<>(prefix + "_watch_cooldown_minutes", () -> store[2], value -> store[2] = value),
          new ConfigGetterSetter<>(prefix + "_time_advancement_ticks", () -> store[3], value -> store[3] = value)
      ));
    }
    return map;
  }

  public static TierConfig get(WatchTier tier) {

    return TIER_CONFIGS.get(tier);
  }

  public record TierConfig(ConfigGetterSetter<Long> durability, ConfigGetterSetter<Long> charges,
                            ConfigGetterSetter<Long> cooldownMinutes, ConfigGetterSetter<Long> timeAdvancementTicks) {

  }

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {

    public T get() {

      return this.getter().get();
    }

    public void set(T value) {

      this.setter().accept(value);
    }
  }
}

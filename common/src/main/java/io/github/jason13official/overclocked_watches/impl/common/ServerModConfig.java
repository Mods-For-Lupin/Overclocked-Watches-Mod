package io.github.jason13official.overclocked_watches.impl.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ServerModConfig {

  public static boolean dayNightCycleAllowed = true;
  public static boolean useLongTimeDelta = true;
  public static long longTimeDelta = 100L; // 500 ? (also ever 5 ticks ????)
  public static long goldenWatchDurability = 100L;
  public static long diamondWatchDurability = 300L;
  public static long netheriteWatchDurability = 500L;
  public static long goldenWatchCharges = 1L;
  public static long diamondWatchCharges = 3L;
  public static long netheriteWatchCharges = 5L;
  public static long goldenWatchCooldownMinutes = 20L;
  public static long diamondWatchCooldownMinutes = 10L;
  public static long netheriteWatchCooldownMinutes = 5L;
  public static long goldenTimeAdvancementTicks = 12_000L;
  public static long diamondTimeAdvancementTicks = 12_000L;
  public static long netheriteTimeAdvancementTicks = 12_000L;

  public static final ConfigGetterSetter<Boolean> DAY_NIGHT_CYCLE_ALLOWED = new ConfigGetterSetter<>("day_night_cycle_allowed", () -> dayNightCycleAllowed, value -> dayNightCycleAllowed = value);
  public static final ConfigGetterSetter<Boolean> USE_LONG_TIME_DELTA = new ConfigGetterSetter<>("use_long_time_delta", () -> useLongTimeDelta, value -> useLongTimeDelta = value);
  public static final ConfigGetterSetter<Long> LONG_TIME_DELTA = new ConfigGetterSetter<>("long_time_delta", () -> longTimeDelta, value -> longTimeDelta = value);
  public static final ConfigGetterSetter<Long> GOLDEN_WATCH_DURABILITY = new ConfigGetterSetter<>("golden_watch_durability", () -> goldenWatchDurability, value -> goldenWatchDurability = value);
  public static final ConfigGetterSetter<Long> DIAMOND_WATCH_DURABILITY = new ConfigGetterSetter<>("diamond_watch_durability", () -> diamondWatchDurability, value -> diamondWatchDurability = value);
  public static final ConfigGetterSetter<Long> NETHERITE_WATCH_DURABILITY = new ConfigGetterSetter<>("netherite_watch_durability", () -> netheriteWatchDurability, value -> netheriteWatchDurability = value);
  public static final ConfigGetterSetter<Long> GOLDEN_WATCH_CHARGES = new ConfigGetterSetter<>("golden_watch_charges", () -> goldenWatchCharges, value -> goldenWatchCharges = value);
  public static final ConfigGetterSetter<Long> DIAMOND_WATCH_CHARGES = new ConfigGetterSetter<>("diamond_watch_charges", () -> diamondWatchCharges, value -> diamondWatchCharges = value);
  public static final ConfigGetterSetter<Long> NETHERITE_WATCH_CHARGES = new ConfigGetterSetter<>("netherite_watch_charges", () -> netheriteWatchCharges, value -> netheriteWatchCharges = value);
  public static final ConfigGetterSetter<Long> GOLDEN_WATCH_COOLDOWN_MINUTES = new ConfigGetterSetter<>("golden_watch_cooldown_minutes", () -> goldenWatchCooldownMinutes, value -> goldenWatchCooldownMinutes = value);
  public static final ConfigGetterSetter<Long> DIAMOND_WATCH_COOLDOWN_MINUTES = new ConfigGetterSetter<>("diamond_watch_cooldown_minutes", () -> diamondWatchCooldownMinutes, value -> diamondWatchCooldownMinutes = value);
  public static final ConfigGetterSetter<Long> NETHERITE_WATCH_COOLDOWN_MINUTES = new ConfigGetterSetter<>("netherite_watch_cooldown_minutes", () -> netheriteWatchCooldownMinutes, value -> netheriteWatchCooldownMinutes = value);
  public static final ConfigGetterSetter<Long> GOLDEN_TIME_ADVANCEMENT_TICKS = new ConfigGetterSetter<>("golden_time_advancement_ticks", () -> goldenTimeAdvancementTicks, value -> goldenTimeAdvancementTicks = value);
  public static final ConfigGetterSetter<Long> DIAMOND_TIME_ADVANCEMENT_TICKS = new ConfigGetterSetter<>("diamond_time_advancement_ticks", () -> diamondTimeAdvancementTicks, value -> diamondTimeAdvancementTicks = value);
  public static final ConfigGetterSetter<Long> NETHERITE_TIME_ADVANCEMENT_TICKS = new ConfigGetterSetter<>("netherite_time_advancement_ticks", () -> netheriteTimeAdvancementTicks, value -> netheriteTimeAdvancementTicks = value);

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {

    public T get() {

      return this.getter().get();
    }

    public void set(T value) {

      this.setter().accept(value);
    }
  }
}

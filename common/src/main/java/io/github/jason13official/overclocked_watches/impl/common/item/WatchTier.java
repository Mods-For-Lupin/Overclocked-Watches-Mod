package io.github.jason13official.overclocked_watches.impl.common.item;


import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import java.util.function.Supplier;

public enum WatchTier {

  GOLDEN(ServerModConfig.GOLDEN_WATCH_DURABILITY.getter(), ServerModConfig.GOLDEN_WATCH_CHARGES.getter(), ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.getter(), ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.getter()),
  DIAMOND(ServerModConfig.DIAMOND_WATCH_DURABILITY.getter(), ServerModConfig.DIAMOND_WATCH_CHARGES.getter(), ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.getter(), ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.getter()),
  NETHERITE(ServerModConfig.NETHERITE_WATCH_DURABILITY.getter(), ServerModConfig.NETHERITE_WATCH_CHARGES.getter(), ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.getter(), ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.getter());

  private final Supplier<Long> itemDurability;
  private final Supplier<Long> watchCharges;
  private final Supplier<Long> timeAdvancementTicks;
  private final Supplier<Long> cooldownMinutes;

  WatchTier(Supplier<Long> itemDurability, Supplier<Long> watchCharges, Supplier<Long> timeAdvancementTicks, Supplier<Long> cooldownMinutes) {
    this.itemDurability = itemDurability;
    this.watchCharges = watchCharges;
    this.timeAdvancementTicks = timeAdvancementTicks;
    this.cooldownMinutes = cooldownMinutes;
  }

  public int getItemDurability() {

    return (int) itemDurability.get().longValue();
  }

  public int getWatchCharges() {

    return (int) watchCharges.get().longValue();
  }

  public int getTimeAdvancementTicks() {

    return (int) timeAdvancementTicks.get().longValue();
  }

  public int getCooldownMinutes() {

    return (int) cooldownMinutes.get().longValue();
  }
}

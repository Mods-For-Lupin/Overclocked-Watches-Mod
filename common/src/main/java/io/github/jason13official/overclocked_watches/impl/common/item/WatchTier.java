package io.github.jason13official.overclocked_watches.impl.common.item;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;

public enum WatchTier {

  GOLDEN, DIAMOND, NETHERITE;

  public int getItemDurability() {

    return (int) ServerModConfig.get(this).durability().get().longValue();
  }

  public int getWatchCharges() {

    return (int) ServerModConfig.get(this).charges().get().longValue();
  }

  public int getTimeAdvancementTicks() {

    return (int) ServerModConfig.get(this).timeAdvancementTicks().get().longValue();
  }

  public int getCooldownMinutes() {

    return (int) ServerModConfig.get(this).cooldownMinutes().get().longValue();
  }
}

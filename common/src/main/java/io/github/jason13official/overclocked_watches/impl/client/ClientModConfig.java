package io.github.jason13official.overclocked_watches.impl.client;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig.ConfigGetterSetter;

public class ClientModConfig {

  public static long defaultDayNightKey = 79;
  public static final ConfigGetterSetter<Long> DEFAULT_DAY_NIGHT_KEY = new ConfigGetterSetter<>("default_day_night_key", () -> defaultDayNightKey, value -> defaultDayNightKey = value);
}

package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;

public class OverclockedWatchesClient {

  public static void init() {
    TimeManager.CLIENT = new TimeManager();
  }
}

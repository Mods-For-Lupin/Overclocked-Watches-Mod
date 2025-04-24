package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.util.TimeManager;

public class OverclockedWatchesClient {

    public static void init() {
        TimeManager.CLIENT = new TimeManager();
    }
}

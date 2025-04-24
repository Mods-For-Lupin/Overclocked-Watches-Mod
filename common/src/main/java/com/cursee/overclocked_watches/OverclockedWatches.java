package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.util.TimeManager;
import net.minecraft.resources.ResourceLocation;

public class OverclockedWatches {

    public static void init() {
        TimeManager.SERVER = new TimeManager();
    }

    public static ResourceLocation identifier(String value) {
        return new ResourceLocation(Constants.MOD_ID, value);
    }
}
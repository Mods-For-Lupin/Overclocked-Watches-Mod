package com.cursee.overclocked_watches;

import net.minecraft.resources.ResourceLocation;

public class OverclockedWatches {

    public static void init() {}

    public static ResourceLocation identifier(String value) {
        return new ResourceLocation(Constants.MOD_ID, value);
    }
}
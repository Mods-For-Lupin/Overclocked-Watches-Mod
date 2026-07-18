package io.github.jason13official.overclocked_watches;

import net.minecraft.resources.ResourceLocation;

public class OverclockedWatches {

  public static void init() {
  }

  public static ResourceLocation identifier(String value) {

    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, value);
  }
}
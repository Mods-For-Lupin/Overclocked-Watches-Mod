package io.github.jason13official.overclocked_watches;

import net.fabricmc.api.ClientModInitializer;

public class OverclockedWatchesClientFabric implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    OverclockedWatchesClient.init();
  }
}

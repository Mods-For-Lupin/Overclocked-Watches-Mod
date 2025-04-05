package com.cursee.overclocked_watches;

import net.fabricmc.api.ClientModInitializer;

public class OverclockedWatchesClientFabric implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        OverclockedWatchesClient.init();
    }
}

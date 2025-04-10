package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.network.FabricNetwork;
import com.cursee.overclocked_watches.core.registry.RegistryFabric;
import com.cursee.monolib.core.sailing.Sailing;
import net.fabricmc.api.ModInitializer;

public class OverclockedWatchesFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        RegistryFabric.register();
        FabricNetwork.Packets.registerC2SReceivers();
    }
}
